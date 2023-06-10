package com.galih.makhrajulhuruf.ui.main

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.MediaController
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.LinearLayoutManager
import com.canhub.cropper.CropImage
import com.fxn.pix.Options
import com.fxn.pix.Pix
import com.galih.makhrajulhuruf.R
import com.galih.makhrajulhuruf.base.BaseActivity
import com.galih.makhrajulhuruf.data.model.ArabCharacter
import com.galih.makhrajulhuruf.databinding.ActivityMainBinding
import com.galih.makhrajulhuruf.ml.Model
import com.galih.makhrajulhuruf.ui.detail.DetailActivity
import com.galih.makhrajulhuruf.ui.list.ArabCharacterAdapter
import com.galih.makhrajulhuruf.ui.list.ListAllCharacterActivity
import com.galih.makhrajulhuruf.utils.characters
import com.galih.makhrajulhuruf.utils.toBinaryArray
import com.galih.makhrajulhuruf.utils.toBitmap
import com.galih.makhrajulhuruf.utils.gone
import com.galih.makhrajulhuruf.utils.labelList
import com.galih.makhrajulhuruf.utils.toBinary
import com.galih.makhrajulhuruf.utils.toGrayscale
import com.galih.makhrajulhuruf.utils.visible
import com.galih.makhrajulhuruf.utils.zhangSuenThinning
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.PictureResult
import com.otaliastudios.cameraview.gesture.Gesture
import com.otaliastudios.cameraview.gesture.GestureAction
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.File
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.exp


@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val characterAdapter: ArabCharacterAdapter by lazy { ArabCharacterAdapter() }

    override val bindingInflater: (LayoutInflater) -> ActivityMainBinding =
        ActivityMainBinding::inflate

    override fun setup() {
//        setupCamera()
        setupRecycler()
        setupAction()
    }

    private fun setupCamera() {
        binding.camera.setLifecycleOwner(this)
        binding.camera.mapGesture(Gesture.PINCH, GestureAction.ZOOM)
        binding.camera.mapGesture(Gesture.TAP, GestureAction.AUTO_FOCUS)
        binding.camera.addCameraListener( object : CameraListener() {
            override fun onPictureTaken(result: PictureResult) {
                val file = File.createTempFile("temp_file", ".jpg", cacheDir)
                result.toFile(file) {
                    if (it == null) return@toFile
                    Picasso.get()
                        .load(it)
                        .memoryPolicy(MemoryPolicy.NO_STORE, MemoryPolicy.NO_CACHE)
                        .resize(300, 300)
                        .centerCrop()
                        .into(binding.imgSelected)
                }
                binding.llCamera.gone()
                binding.linChooseAction.gone()
                binding.linRecognizeAction.visible()
            }
        })
    }

    private fun setupRecycler() {
        with(binding.rvResult) {
            adapter = characterAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        characterAdapter.setOnItemClickListener {
            goToActivity(DetailActivity::class.java, Bundle().apply {
                putParcelable(DetailActivity.EXTRA_DATA, it)
            })
        }
    }

    private fun setupAction() {
        with(binding) {
            btnChooseImage.setOnClickListener { chooseImage() }
            btnChangeImage.setOnClickListener { chooseImage() }
            btnRecognize.setOnClickListener {
                handlePreprocessing()
                handleRecognition()
            }
            binding.fabTakePicture.setOnClickListener { binding.camera.takePicture() }
            btnListAll.setOnClickListener { goToActivity(ListAllCharacterActivity::class.java) }
            btnReset.setOnClickListener {
                video.suspend()
                btnReset.gone()
                llResult.gone()
                imgSelected.setImageDrawable(resources.getDrawable(R.drawable.img_placeholder))
                characterAdapter.differ.submitList(null)
                linChooseAction.visible()
            }
        }
    }

    private fun chooseImage() {
        askPermissions(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            onAccepted = {
                Pix.start(this@MainActivity,
                    Options.init()
                        .setRequestCode(CAMERA_REQUEST_CODE)
                        .setMode(Options.Mode.Picture))
            }
        )

//        binding.llCamera.visible()
    }

    private fun handlePreprocessing() {
        binding.imgSelected.invalidate()
        val drawable = binding.imgSelected.drawable
        val image = drawable.toBitmap()
        val binaryArray = image.toGrayscale().toBinary().toBinaryArray()
        val result = zhangSuenThinning(binaryArray).toBitmap(image.width, image.height)

        binding.imgSelected.setImageBitmap(result)
    }

    private fun handleRecognition() {
        binding.imgSelected.invalidate()
        val model = Model.newInstance(applicationContext)
        val drawable = binding.imgSelected.drawable
        val image = drawable.toBitmap()

        // Creates inputs for reference.
        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 300, 300, 3), DataType.FLOAT32)
        val byteBuffer = ByteBuffer.allocateDirect(4 * IMAGE_SIZE * IMAGE_SIZE * 3)
        byteBuffer.order(ByteOrder.nativeOrder())
        val intValues = IntArray(IMAGE_SIZE * IMAGE_SIZE)
        image.getPixels(intValues, 0, image.width, 0, 0, image.width, image.height)
        var pixel = 0
        //iterate over each pixel and extract R, G, and B values. Add those values individually to the byte buffer.
        for (i in 0 until IMAGE_SIZE) {
            for (j in 0 until IMAGE_SIZE) {
                val `val` = intValues[pixel++] // RGB
                byteBuffer.putFloat((`val` shr 16 and 0xFF) * (1f / 1))
                byteBuffer.putFloat((`val` shr 8 and 0xFF) * (1f / 1))
                byteBuffer.putFloat((`val` and 0xFF) * (1f / 1))
            }
        }

        inputFeature0.loadBuffer(byteBuffer)

        // Runs model inference and gets result.
        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer

        val logits = outputFeature0.floatArray
        val confidences = softmax(logits)
        // find the index of the class with the biggest confidence.
        var maxPos = 0
        var maxConfidence = 0f
        for (i in confidences.indices) {
            Log.d("coba", "handleRecognition: ${labelList[i]}: ${confidences[i]}")
            if (confidences[i] > maxConfidence) {
                maxConfidence = confidences[i]
                maxPos = i
            }
        }
        val resLabel = labelList[maxPos]
        Log.d("coba", "handleRecognition: $resLabel $maxConfidence")

        if (maxConfidence > THRESHOLD) {
            val data = characters.filter { it.labels.contains(resLabel) }[0]
            populateResult(data)
        } else {
            binding.tvName.text = "Karakter tidak terdeteksi"
            binding.imgCharacter.setImageResource(R.drawable.ic_report_problem)
            binding.llResult.visible()
        }

        binding.btnReset.visible()
        binding.linRecognizeAction.gone()

        // Releases model resources if no longer used.
        model.close()
    }

    private fun populateResult(data: ArabCharacter) {
        with(binding) {
            tvName.text = data.name
            Picasso.get().load(data.image).into(imgCharacter)
            video.setVideoPath(data.video)
            val mediaController = MediaController(this@MainActivity)
            mediaController.setAnchorView(video)
            mediaController.setMediaPlayer(video)
            video.setMediaController(mediaController)
            video.start()
            llResult.visible()
        }
    }

    private fun softmax(logits: FloatArray): FloatArray {
        val result = FloatArray(logits.size)
        var sum = 0f
        for (i in logits.indices) {
            result[i] = exp(logits[i].toDouble()).toFloat()
            sum += result[i]
        }
        for (i in result.indices) {
            result[i] /= sum
        }
        return result
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CAMERA_REQUEST_CODE) {
                val returnValue: ArrayList<String> = data?.getStringArrayListExtra(Pix.IMAGE_RESULTS) as ArrayList<String>
                CropImage.activity(Uri.fromFile(File(returnValue[0])))
                    .setAspectRatio(1, 1)
                    .start(this)
            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                val result = CropImage.getActivityResult(data)
                if (result != null) {
                    val file = File(result.getUriFilePath(this)!!)
                    with(binding) {
                        Picasso.get()
                            .load(file)
                            .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                            .resize(300, 300)
                            .centerCrop()
                            .into(imgSelected)
                        linChooseAction.gone()
                        linRecognizeAction.visible()
                    }
                }
            }
        }
    }

    companion object {
        const val IMAGE_SIZE = 300
        const val CAMERA_REQUEST_CODE = 3
        const val THRESHOLD = 0.8F
    }
}