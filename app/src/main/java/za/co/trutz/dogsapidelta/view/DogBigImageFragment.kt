package za.co.trutz.dogsapidelta.view

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import za.co.trutz.dogsapidelta.R
import za.co.trutz.dogsapidelta.databinding.FragmentDogImageBinding
import java.io.ByteArrayOutputStream
import java.io.File

class DogBigImageFragment : Fragment() {

    private var _binding: FragmentDogImageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDogImageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments != null) {
            val imageUrl = arguments!!.get("argImageUrl")

            Glide.with(requireContext())
                .load(imageUrl)
                .placeholder(R.drawable.paw_placeholder)
                .error(R.drawable.paw_placeholder)
                .centerCrop()
                .into(binding.dogImageView)

            shareImage()

            binding.buttonDownload.setOnClickListener {
                downloadImage(imageUrl.toString())
            }
        }
    }

    private fun shareImage() {
        binding.buttonShare.setOnClickListener {

            val bitmap = binding.dogImageView.drawable.toBitmap()
            val bytes = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)

            val path = MediaStore.Images.Media.insertImage(
                requireContext().contentResolver,
                bitmap,
                "tempimage",
                "doggy images from app"
            )
            val uri = Uri.parse(path)

            val intent = Intent(Intent.ACTION_SEND).setType("image/*")
            intent.putExtra(Intent.EXTRA_STREAM, uri)
            startActivity(intent)
        }
    }

    private fun downloadImage(url: String) {

        val directory = File(Environment.DIRECTORY_PICTURES)

        if (!directory.exists()) {
            directory.mkdirs()
        }

        val downloadManager =
            requireContext().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        val downloadUri = Uri.parse(url)

        val request = DownloadManager.Request(downloadUri).apply {
            setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle(url.substring(url.lastIndexOf("/") + 1))
                .setDescription("")
                .setDestinationInExternalPublicDir(
                    directory.toString(),
                    url.substring(url.lastIndexOf("/") + 1)
                )
        }

        var msg: String?
        var lastMsg = ""

        val downloadId = downloadManager.enqueue(request)
        val query = DownloadManager.Query().setFilterById(downloadId)
        Thread(Runnable {
            var downloading = true
            while (downloading) {
                val cursor: Cursor = downloadManager.query(query)
                cursor.moveToFirst()
                if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                    downloading = false
                }
                val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                msg = imageDownloadStatusMessage(url, directory, status)
                if (msg != lastMsg) {
                    requireActivity().runOnUiThread {
                        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
                    }
                    lastMsg = msg ?: ""
                }
                cursor.close()
            }
        }).start()
    }

    private fun imageDownloadStatusMessage(url: String, directory: File, status: Int): String {
        val msg = when (status) {
            DownloadManager.STATUS_PENDING -> "Preparing download..."
            DownloadManager.STATUS_PAUSED -> "Paused"
            DownloadManager.STATUS_RUNNING -> "Downloading..."
            DownloadManager.STATUS_FAILED -> "Download has been failed."
            DownloadManager.STATUS_SUCCESSFUL -> "Image downloaded successfully in $directory" + File.separator + url.substring(
                url.lastIndexOf("/") + 1
            )
            else -> "Image format can not be downloaded."
        }
        return msg
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}