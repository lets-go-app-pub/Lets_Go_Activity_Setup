package com.example.seticonsactivitiesandcategoriesletsgo

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.protobuf.ByteString
import delete_me.DeleteMe
import delete_me_bi_di.DeleteMeBiDi
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import server_specific.SetServerActivityOrCategoryRequest
import server_specific.SetServerIconRequest
import testing_bi_di.TestingBiDi
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.nio.charset.Charset
import java.util.*
import kotlin.collections.ArrayList

interface MyInterface {
    suspend fun foo(): Flow<Int>
}

class MyIntermediate: MyInterface {
    override suspend fun foo(): Flow<Int> {
        return MyFunction().foo()
    }
}

class MyFunction {
    suspend fun foo(): Flow<Int> = flow {
        repeat(5) {
            Log.i("flowTest", "running $it")
            delay(1000)
            emit(it)
        }
    }
}

data class TempClass(val one: Int)

class MainActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val newList = listOf( "zero", "one", "two", "three")

        val listResponse = DeleteMe.DeleteMeResponse.newBuilder()
            .addAllDeleteMeList(newList)
            .build()

        Log.i("testingRemoveList", "${listResponse.deleteMeListList}")

        val client = ClientRPC();

        Log.i("uuid_stff", UUID.randomUUID().toString())

        CoroutineScope(Dispatchers.IO).launch {
            //client.testingStreamBiDi();
            //client.deleteMeUnary()
            //client.deleteMeServer()
            //client.deleteMeClient()
        }

        firstTextView.text = "Text View"

        categoriesButton.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
//                if(client.requestObserverDeleteMe != null) {
//
//                    Log.i("deleteMeStream", "client.incrementMe: ${client.incrementMe}")
//                    val stuff = TestingBiDi.TestingBiDiRequest.newBuilder()
//                        .setRequestMessageInfo(TestingBiDi.RequestMessageInfoRequest.newBuilder()
//                            .setMessage("sending_this_message"))
//                        .build()
//
//                        client.requestObserverDeleteMe!!.onNext(stuff)
//                }
            }
            sendCategoriesToServer(firstTextView)
        }

        activitiesButton.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                //client.requestObserverDeleteMe!!.onCompleted()
            }
            sendActivitiesToServer(firstTextView)
        }

        iconsButton.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                //client.testingStreamBiDi()
            }
            sendIconsToServer(firstTextView)
        }
    }

    private fun sendCategoriesToServer(firstTextView: TextView) {

        firstTextView.text = "Sending Categories..."

        val passedActivityCategoryObj = ArrayList<SetServerActivityOrCategoryRequest>()

        passedActivityCategoryObj.add(
            SetServerActivityOrCategoryRequest.newBuilder()
                .setPassword(ClientRPC.password)
                .setDeleteThis(false)
                .setName("Fitness")
                .setMinAge(13)
                .setColor("#8D4E76")
                .setCategoryIndex(0)
                .setIconIndex(0)
                .build()
        )

        passedActivityCategoryObj.add(
            SetServerActivityOrCategoryRequest.newBuilder()
                .setPassword(ClientRPC.password)
                .setDeleteThis(false)
                .setName("Sports")
                .setMinAge(13)
                .setColor("#CEA93C")
                .setCategoryIndex(0)
                .setIconIndex(0)
                .build()
        )

        passedActivityCategoryObj.add(
            SetServerActivityOrCategoryRequest.newBuilder()
                .setPassword(ClientRPC.password)
                .setDeleteThis(false)
                .setName("Animals")
                .setMinAge(13)
                .setColor("#A1712E")
                .setCategoryIndex(0)
                .setIconIndex(0)
                .build()
        )

        passedActivityCategoryObj.add(
            SetServerActivityOrCategoryRequest.newBuilder()
                .setPassword(ClientRPC.password)
                .setDeleteThis(false)
                .setName("Art")
                .setMinAge(13)
                .setColor("#415B97")
                .setCategoryIndex(0)
                .setIconIndex(0)
                .build()
        )

        val clientRPC = ClientRPC()

        val result = clientRPC.setCategories(passedActivityCategoryObj.toList())

        if (result != null) {
            if (result.successful) {
                firstTextView.text = "Successfully sent categories.\n"
            } else {
                firstTextView.text = "Failed to send categories.\n"
            }
        }

    }

    private fun sendActivitiesToServer(firstTextView: TextView) {

        firstTextView.text = "Sending Activities..."

        val passedActivityCategoryObj = ArrayList<SetServerActivityOrCategoryRequest>()

        passedActivityCategoryObj.add(
            SetServerActivityOrCategoryRequest.newBuilder()
                .setPassword(ClientRPC.password)
                .setDeleteThis(false)
                .setName("Swimming")
                .setMinAge(13)
                .setCategoryIndex(0)
                .setIconIndex(0)
                .build()
        )

        passedActivityCategoryObj.add(
            SetServerActivityOrCategoryRequest.newBuilder()
                .setPassword(ClientRPC.password)
                .setDeleteThis(false)
                .setName("Biking")
                .setMinAge(13)
                .setCategoryIndex(0)
                .setIconIndex(1)
                .build()
        )

        passedActivityCategoryObj.add(
            SetServerActivityOrCategoryRequest.newBuilder()
                .setPassword(ClientRPC.password)
                .setDeleteThis(false)
                .setName("Weight Training")
                .setMinAge(57)
                .setCategoryIndex(0)
                .setIconIndex(2)
                .build()
        )

        passedActivityCategoryObj.add(
            SetServerActivityOrCategoryRequest.newBuilder()
                .setPassword(ClientRPC.password)
                .setDeleteThis(false)
                .setName("Baseball")
                .setMinAge(13)
                .setCategoryIndex(1)
                .setIconIndex(3)
                .build()
        )

        passedActivityCategoryObj.add(
            SetServerActivityOrCategoryRequest.newBuilder()
                .setPassword(ClientRPC.password)
                .setDeleteThis(false)
                .setName("Table Tennis")
                .setMinAge(18)
                .setCategoryIndex(1)
                .setIconIndex(4)
                .build()
        )


        passedActivityCategoryObj.add(
            SetServerActivityOrCategoryRequest.newBuilder()
                .setPassword(ClientRPC.password)
                .setDeleteThis(false)
                .setName("Cat Eating")
                .setMinAge(18)
                .setCategoryIndex(2)
                .setIconIndex(5)
                .build()
        )

        passedActivityCategoryObj.add(
            SetServerActivityOrCategoryRequest.newBuilder()
                .setPassword(ClientRPC.password)
                .setDeleteThis(false)
                .setName("Bird Watching")
                .setMinAge(18)
                .setCategoryIndex(2)
                .setIconIndex(6)
                .build()
        )

        passedActivityCategoryObj.add(
            SetServerActivityOrCategoryRequest.newBuilder()
                .setPassword(ClientRPC.password)
                .setDeleteThis(false)
                .setName("Dog Walking")
                .setMinAge(18)
                .setCategoryIndex(2)
                .setIconIndex(7)
                .build()
        )

        passedActivityCategoryObj.add(
            SetServerActivityOrCategoryRequest.newBuilder()
                .setPassword(ClientRPC.password)
                .setDeleteThis(false)
                .setName("Dragon Riding")
                .setMinAge(18)
                .setCategoryIndex(2)
                .setIconIndex(8)
                .build()
        )

        passedActivityCategoryObj.add(
            SetServerActivityOrCategoryRequest.newBuilder()
                .setPassword(ClientRPC.password)
                .setDeleteThis(false)
                .setName("Fishing")
                .setMinAge(18)
                .setCategoryIndex(2)
                .setIconIndex(9)
                .build()
        )

        passedActivityCategoryObj.add(
            SetServerActivityOrCategoryRequest.newBuilder()
                .setPassword(ClientRPC.password)
                .setDeleteThis(false)
                .setName("Hippo Wrangling")
                .setMinAge(18)
                .setCategoryIndex(2)
                .setIconIndex(10)
                .build()
        )

        passedActivityCategoryObj.add(
            SetServerActivityOrCategoryRequest.newBuilder()
                .setPassword(ClientRPC.password)
                .setDeleteThis(false)
                .setName("Painting")
                .setMinAge(18)
                .setCategoryIndex(3)
                .setIconIndex(11)
                .build()
        )

        val clientRPC = ClientRPC()

        val result = clientRPC.setActivities(passedActivityCategoryObj.toList())

        if (result != null) {
            if (result.successful) {
                firstTextView.text = "Successfully sent activities.\n"
            } else {
                firstTextView.text = "Failed to send activities.\n"
            }
        }

    }

    private fun sendIconsToServer(firstTextView: TextView) {

        firstTextView.text = "Sending Icons..."

        val convertResourceIDToByteArray: (Int) -> ByteString = { resourceID ->

            val baos = ByteArrayOutputStream()
            val bitmap = BitmapFactory.decodeResource(resources, resourceID)

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
            val imageInBytes = baos.toByteArray()

            Log.i("convert", "imageInBytes.size: ${imageInBytes.size}")
            ByteString.copyFrom(imageInBytes)

        }

        val passedIconObj = ArrayList<SetServerIconRequest>()
        var tempBasicImage = convertResourceIDToByteArray(R.drawable.swimmer_solid)
        var tempInvertedImage = convertResourceIDToByteArray(R.drawable.swimmer_solid_blue)
        var tempNoBorderImage = convertResourceIDToByteArray(R.drawable.swimmer_solid)

        passedIconObj.add( //table tennis
            SetServerIconRequest.newBuilder()
                .setPassword(ClientRPC.password)
                .setPushBack(true)
                .setIndexNumber(-1)
                .setIconActive(true)
                .setCompressedImage(tempBasicImage)
                .setCompressedImageSize(tempBasicImage.size())
                .setCompressedImageInverted(tempInvertedImage)
                .setCompressedImageInvertedSize(tempInvertedImage.size())
                .setCompressedImageNoBorder(tempNoBorderImage)
                .setCompressedImageNoBorderSize(tempNoBorderImage.size())
                .build()
        )

        tempBasicImage = convertResourceIDToByteArray(R.drawable.biking_solid)
        tempInvertedImage = convertResourceIDToByteArray(R.drawable.biking_solid_blue)
        tempNoBorderImage = convertResourceIDToByteArray(R.drawable.biking_solid)

        passedIconObj.add( //table tennis
            SetServerIconRequest.newBuilder()
                .setPassword(ClientRPC.password)
                .setPushBack(true)
                .setIndexNumber(-1)
                .setIconActive(true)
                .setCompressedImage(tempBasicImage)
                .setCompressedImageSize(tempBasicImage.size())
                .setCompressedImageInverted(tempInvertedImage)
                .setCompressedImageInvertedSize(tempInvertedImage.size())
                .setCompressedImageNoBorder(tempNoBorderImage)
                .setCompressedImageNoBorderSize(tempNoBorderImage.size())
                .build()
        )

        tempBasicImage = convertResourceIDToByteArray(R.drawable.dumbbell_solid)
        tempInvertedImage = convertResourceIDToByteArray(R.drawable.dumbbell_solid_blue)
        tempNoBorderImage = convertResourceIDToByteArray(R.drawable.dumbbell_solid)

        passedIconObj.add( //table tennis
            SetServerIconRequest.newBuilder()
                .setPassword(ClientRPC.password)
                .setPushBack(true)
                .setIndexNumber(-1)
                .setIconActive(true)
                .setCompressedImage(tempBasicImage)
                .setCompressedImageSize(tempBasicImage.size())
                .setCompressedImageInverted(tempInvertedImage)
                .setCompressedImageInvertedSize(tempInvertedImage.size())
                .setCompressedImageNoBorder(tempNoBorderImage)
                .setCompressedImageNoBorderSize(tempNoBorderImage.size())
                .build()
        )

        tempBasicImage = convertResourceIDToByteArray(R.drawable.baseball_ball_solid)
        tempInvertedImage = convertResourceIDToByteArray(R.drawable.baseball_ball_solid_blue)
        tempNoBorderImage = convertResourceIDToByteArray(R.drawable.baseball_ball_solid)

        passedIconObj.add( //table tennis
            SetServerIconRequest.newBuilder()
                .setPassword(ClientRPC.password)
                .setPushBack(true)
                .setIndexNumber(-1)
                .setIconActive(true)
                .setCompressedImage(tempBasicImage)
                .setCompressedImageSize(tempBasicImage.size())
                .setCompressedImageInverted(tempInvertedImage)
                .setCompressedImageInvertedSize(tempInvertedImage.size())
                .setCompressedImageNoBorder(tempNoBorderImage)
                .setCompressedImageNoBorderSize(tempNoBorderImage.size())
                .build()
        )

        tempBasicImage = convertResourceIDToByteArray(R.drawable.table_tennis_solid)
        tempInvertedImage = convertResourceIDToByteArray(R.drawable.table_tennis_solid_blue)
        tempNoBorderImage = convertResourceIDToByteArray(R.drawable.table_tennis_solid)

        passedIconObj.add( //table tennis
            SetServerIconRequest.newBuilder()
                .setPassword(ClientRPC.password)
                .setPushBack(true)
                .setIndexNumber(-1)
                .setIconActive(true)
                .setCompressedImage(tempBasicImage)
                .setCompressedImageSize(tempBasicImage.size())
                .setCompressedImageInverted(tempInvertedImage)
                .setCompressedImageInvertedSize(tempInvertedImage.size())
                .setCompressedImageNoBorder(tempNoBorderImage)
                .setCompressedImageNoBorderSize(tempNoBorderImage.size())
                .build()
        )

        tempBasicImage = convertResourceIDToByteArray(R.drawable.cat_solid)
        tempInvertedImage = convertResourceIDToByteArray(R.drawable.cat_solid_blue)
        tempNoBorderImage = convertResourceIDToByteArray(R.drawable.cat_solid)

        passedIconObj.add( //cat
            SetServerIconRequest.newBuilder()
                .setPassword(ClientRPC.password)
                .setPushBack(true)
                .setIndexNumber(-1)
                .setIconActive(true)
                .setCompressedImage(tempBasicImage)
                .setCompressedImageSize(tempBasicImage.size())
                .setCompressedImageInverted(tempInvertedImage)
                .setCompressedImageInvertedSize(tempInvertedImage.size())
                .setCompressedImageNoBorder(tempNoBorderImage)
                .setCompressedImageNoBorderSize(tempNoBorderImage.size())
                .build()
        )

        tempBasicImage = convertResourceIDToByteArray(R.drawable.crow_solid)
        tempInvertedImage = convertResourceIDToByteArray(R.drawable.crow_solid_blue)
        tempNoBorderImage = convertResourceIDToByteArray(R.drawable.crow_solid)

        passedIconObj.add( //bird
            SetServerIconRequest.newBuilder()
                .setPassword(ClientRPC.password)
                .setPushBack(true)
                .setIndexNumber(-1)
                .setIconActive(true)
                .setCompressedImage(tempBasicImage)
                .setCompressedImageSize(tempBasicImage.size())
                .setCompressedImageInverted(tempInvertedImage)
                .setCompressedImageInvertedSize(tempInvertedImage.size())
                .setCompressedImageNoBorder(tempNoBorderImage)
                .setCompressedImageNoBorderSize(tempNoBorderImage.size())
                .build()
        )

        tempBasicImage = convertResourceIDToByteArray(R.drawable.dog_solid)
        tempInvertedImage = convertResourceIDToByteArray(R.drawable.dog_solid_blue)
        tempNoBorderImage = convertResourceIDToByteArray(R.drawable.dog_solid)

        passedIconObj.add( //dog
            SetServerIconRequest.newBuilder()
                .setPassword(ClientRPC.password)
                .setPushBack(true)
                .setIndexNumber(-1)
                .setIconActive(true)
                .setCompressedImage(tempBasicImage)
                .setCompressedImageSize(tempBasicImage.size())
                .setCompressedImageInverted(tempInvertedImage)
                .setCompressedImageInvertedSize(tempInvertedImage.size())
                .setCompressedImageNoBorder(tempNoBorderImage)
                .setCompressedImageNoBorderSize(tempNoBorderImage.size())
                .build()
        )

        tempBasicImage = convertResourceIDToByteArray(R.drawable.dragon_solid)
        tempInvertedImage = convertResourceIDToByteArray(R.drawable.dragon_solid_blue)
        tempNoBorderImage = convertResourceIDToByteArray(R.drawable.dragon_solid)

        passedIconObj.add( //dragon
            SetServerIconRequest.newBuilder()
                .setPassword(ClientRPC.password)
                .setPushBack(true)
                .setIndexNumber(-1)
                .setIconActive(true)
                .setCompressedImage(tempBasicImage)
                .setCompressedImageSize(tempBasicImage.size())
                .setCompressedImageInverted(tempInvertedImage)
                .setCompressedImageInvertedSize(tempInvertedImage.size())
                .setCompressedImageNoBorder(tempNoBorderImage)
                .setCompressedImageNoBorderSize(tempNoBorderImage.size())
                .build()
        )

        tempBasicImage = convertResourceIDToByteArray(R.drawable.fish_solid)
        tempInvertedImage = convertResourceIDToByteArray(R.drawable.fish_solid_blue)
        tempNoBorderImage = convertResourceIDToByteArray(R.drawable.fish_solid)

        passedIconObj.add( //fish
            SetServerIconRequest.newBuilder()
                .setPassword(ClientRPC.password)
                .setPushBack(true)
                .setIndexNumber(-1)
                .setIconActive(true)
                .setCompressedImage(tempBasicImage)
                .setCompressedImageSize(tempBasicImage.size())
                .setCompressedImageInverted(tempInvertedImage)
                .setCompressedImageInvertedSize(tempInvertedImage.size())
                .setCompressedImageNoBorder(tempNoBorderImage)
                .setCompressedImageNoBorderSize(tempNoBorderImage.size())
                .build()
        )

        tempBasicImage = convertResourceIDToByteArray(R.drawable.hippo_solid)
        tempInvertedImage = convertResourceIDToByteArray(R.drawable.hippo_solid_blue)
        tempNoBorderImage = convertResourceIDToByteArray(R.drawable.hippo_solid)

        passedIconObj.add( //hippo
            SetServerIconRequest.newBuilder()
                .setPassword(ClientRPC.password)
                .setPushBack(true)
                .setIndexNumber(-1)
                .setIconActive(true)
                .setCompressedImage(tempBasicImage)
                .setCompressedImageSize(tempBasicImage.size())
                .setCompressedImageInverted(tempInvertedImage)
                .setCompressedImageInvertedSize(tempInvertedImage.size())
                .setCompressedImageNoBorder(tempNoBorderImage)
                .setCompressedImageNoBorderSize(tempNoBorderImage.size())
                .build()
        )

        tempBasicImage = convertResourceIDToByteArray(R.drawable.palette_solid)
        tempInvertedImage = convertResourceIDToByteArray(R.drawable.palette_solid_blue)
        tempNoBorderImage = convertResourceIDToByteArray(R.drawable.palette_solid)

        passedIconObj.add( //painting
            SetServerIconRequest.newBuilder()
                .setPassword(ClientRPC.password)
                .setPushBack(true)
                .setIndexNumber(-1)
                .setIconActive(true)
                .setCompressedImage(tempBasicImage)
                .setCompressedImageSize(tempBasicImage.size())
                .setCompressedImageInverted(tempInvertedImage)
                .setCompressedImageInvertedSize(tempInvertedImage.size())
                .setCompressedImageNoBorder(tempNoBorderImage)
                .setCompressedImageNoBorderSize(tempNoBorderImage.size())
                .build()
        )

        val clientRPC = ClientRPC()

        val result = clientRPC.setIcons(passedIconObj.toList())

        if (result != null) {
            if (result.successful) {
                firstTextView.text = "Successfully sent icons.\n"
            } else {
                firstTextView.text = "Failed to send icons.\n"
            }
        }

    }

}

object StoringStuff {

    var num1: ByteArray = byteArrayOf()
    var num2: ByteArray = byteArrayOf()
    var num3: ByteArray = byteArrayOf()
}


/** Categories
//passedActivityCategoryObj.add(
//SetServerActivityOrCategoryRequest.newBuilder()
//.setPassword(ClientRPC.password)
//.setDeleteThis(false)
//.setName("Fitness")
//.setMinAge(13)
//.setColor("#8D4E76")
//.setCategoryIndex(0)
//.setIconIndex(0)
//.build()
//)
//
//passedActivityCategoryObj.add(
//SetServerActivityOrCategoryRequest.newBuilder()
//.setPassword(ClientRPC.password)
//.setDeleteThis(false)
//.setName("Sports")
//.setMinAge(13)
//.setColor("#CEA93C")
//.setCategoryIndex(0)
//.setIconIndex(0)
//.build()
//)
//
//passedActivityCategoryObj.add(
//SetServerActivityOrCategoryRequest.newBuilder()
//.setPassword(ClientRPC.password)
//.setDeleteThis(false)
//.setName("Animals")
//.setMinAge(13)
//.setColor("#A1712E")
//.setCategoryIndex(0)
//.setIconIndex(0)
//.build()
//)
//
//passedActivityCategoryObj.add(
//SetServerActivityOrCategoryRequest.newBuilder()
//.setPassword(ClientRPC.password)
//.setDeleteThis(false)
//.setName("Art")
//.setMinAge(13)
//.setColor("#415B97")
//.setCategoryIndex(0)
//.setIconIndex(0)
//.build()
//)
 **/

/** Activities
//        passedActivityCategoryObj.add(
//        SetServerActivityOrCategoryRequest.newBuilder()
//        .setPassword(ClientRPC.password)
//        .setDeleteThis(false)
//        .setName("Swimming")
//        .setMinAge(13)
//        .setCategoryIndex(0)
//        .setIconIndex(0)
//        .build()
//        )
//
//        passedActivityCategoryObj.add(
//        SetServerActivityOrCategoryRequest.newBuilder()
//        .setPassword(ClientRPC.password)
//        .setDeleteThis(false)
//        .setName("Biking")
//        .setMinAge(13)
//        .setCategoryIndex(0)
//        .setIconIndex(1)
//        .build()
//        )
//
//        passedActivityCategoryObj.add(
//        SetServerActivityOrCategoryRequest.newBuilder()
//        .setPassword(ClientRPC.password)
//        .setDeleteThis(false)
//        .setName("Weight Training")
//        .setMinAge(57)
//        .setCategoryIndex(0)
//        .setIconIndex(2)
//        .build()
//        )
//
//        passedActivityCategoryObj.add(
//        SetServerActivityOrCategoryRequest.newBuilder()
//        .setPassword(ClientRPC.password)
//        .setDeleteThis(false)
//        .setName("Baseball")
//        .setMinAge(13)
//        .setCategoryIndex(1)
//        .setIconIndex(3)
//        .build()
//        )
//
//        passedActivityCategoryObj.add(
//        SetServerActivityOrCategoryRequest.newBuilder()
//        .setPassword(ClientRPC.password)
//        .setDeleteThis(false)
//        .setName("Table Tennis")
//        .setMinAge(18)
//        .setCategoryIndex(1)
//        .setIconIndex(4)
//        .build()
//        )
//
//
//        passedActivityCategoryObj.add(
//        SetServerActivityOrCategoryRequest.newBuilder()
//        .setPassword(ClientRPC.password)
//        .setDeleteThis(false)
//        .setName("Cat Eating")
//        .setMinAge(18)
//        .setCategoryIndex(2)
//        .setIconIndex(5)
//        .build()
//        )
//
//        passedActivityCategoryObj.add(
//        SetServerActivityOrCategoryRequest.newBuilder()
//        .setPassword(ClientRPC.password)
//        .setDeleteThis(false)
//        .setName("Bird Watching")
//        .setMinAge(18)
//        .setCategoryIndex(2)
//        .setIconIndex(6)
//        .build()
//        )
//
//        passedActivityCategoryObj.add(
//        SetServerActivityOrCategoryRequest.newBuilder()
//        .setPassword(ClientRPC.password)
//        .setDeleteThis(false)
//        .setName("Dog Walking")
//        .setMinAge(18)
//        .setCategoryIndex(2)
//        .setIconIndex(7)
//        .build()
//        )
//
//        passedActivityCategoryObj.add(
//        SetServerActivityOrCategoryRequest.newBuilder()
//        .setPassword(ClientRPC.password)
//        .setDeleteThis(false)
//        .setName("Dragon Riding")
//        .setMinAge(18)
//        .setCategoryIndex(2)
//        .setIconIndex(8)
//        .build()
//        )
//
//        passedActivityCategoryObj.add(
//        SetServerActivityOrCategoryRequest.newBuilder()
//        .setPassword(ClientRPC.password)
//        .setDeleteThis(false)
//        .setName("Fishing")
//        .setMinAge(18)
//        .setCategoryIndex(2)
//        .setIconIndex(9)
//        .build()
//        )
//
//        passedActivityCategoryObj.add(
//        SetServerActivityOrCategoryRequest.newBuilder()
//        .setPassword(ClientRPC.password)
//        .setDeleteThis(false)
//        .setName("Hippo Wrangling")
//        .setMinAge(18)
//        .setCategoryIndex(2)
//        .setIconIndex(10)
//        .build()
//        )
//
//        passedActivityCategoryObj.add(
//        SetServerActivityOrCategoryRequest.newBuilder()
//        .setPassword(ClientRPC.password)
//        .setDeleteThis(false)
//        .setName("Painting")
//        .setMinAge(18)
//        .setCategoryIndex(3)
//        .setIconIndex(11)
//        .build()
//        )

 * **/

/**
 * REQUEST ICONS
val channel = ManagedChannelBuilder.forAddress("10.0.2.2", 50051)
.usePlaintext()
.build()

val request = ServerIconsRequest.newBuilder()
.setLetsGoVersion(1.0)
.setLoggedInToken("5f29ba765b2a0000a6005f84")
.addIconIndex(0)
.build()

val client =
RequestFieldsServiceGrpc.newBlockingStub(channel)

val responseIterator =
client.requestServerIconsRPC(request)

var theIndex = 0
while (responseIterator.hasNext()) {
val nextResponse = responseIterator.next()
if (theIndex == 0) {

val pic = nextResponse.compressedImage.toByteArray()
val bitmap = BitmapFactory.decodeByteArray(pic, 0, pic.size)
imageView.setImageBitmap(bitmap)

val pic2 = nextResponse.compressedImageInverted.toByteArray()
val bitmap2 = BitmapFactory.decodeByteArray(pic2, 0, pic2.size)
imageView2.setImageBitmap(bitmap2)

imageView3.setImageResource(R.drawable.swimmer_solid)

} else if (theIndex == 1) {
//num2 = nextResponse.compressedImage.toByteArray()
} else if (theIndex == 2) {
//num3 = nextResponse.compressedImage.toByteArray()
}

theIndex++

}
 **/

//all icons

//        passedIconObj.add( //table tennis
//            SetServerIconRequest.newBuilder()
//                .setPassword(ClientRPC.password)
//                .setPushBack(true)
//                .setIndexNumber(-1)
//                .setIconActive(true)
//                .setCompressedImage(convertResourceIDToByteArray(R.drawable.swimmer_solid))
//                .setCompressedImageInverted(convertResourceIDToByteArray(R.drawable.swimmer_solid_blue))
//                .setCompressedImageNoBorder(convertResourceIDToByteArray(R.drawable.swimmer_solid))
//                .build()
//        )
//
//        passedIconObj.add( //table tennis
//            SetServerIconRequest.newBuilder()
//                .setPassword(ClientRPC.password)
//                .setPushBack(true)
//                .setIndexNumber(-1)
//                .setIconActive(true)
//                .setCompressedImage(convertResourceIDToByteArray(R.drawable.biking_solid))
//                .setCompressedImageInverted(convertResourceIDToByteArray(R.drawable.biking_solid_blue))
//                .setCompressedImageNoBorder(convertResourceIDToByteArray(R.drawable.biking_solid))
//                .build()
//        )
//
//        passedIconObj.add( //table tennis
//            SetServerIconRequest.newBuilder()
//                .setPassword(ClientRPC.password)
//                .setPushBack(true)
//                .setIndexNumber(-1)
//                .setIconActive(true)
//                .setCompressedImage(convertResourceIDToByteArray(R.drawable.dumbbell_solid))
//                .setCompressedImageInverted(convertResourceIDToByteArray(R.drawable.dumbbell_solid_blue))
//                .setCompressedImageNoBorder(convertResourceIDToByteArray(R.drawable.dumbbell_solid))
//                .build()
//        )
//
//        passedIconObj.add( //table tennis
//            SetServerIconRequest.newBuilder()
//                .setPassword(ClientRPC.password)
//                .setPushBack(true)
//                .setIndexNumber(-1)
//                .setIconActive(true)
//                .setCompressedImage(convertResourceIDToByteArray(R.drawable.baseball_ball_solid))
//                .setCompressedImageInverted(convertResourceIDToByteArray(R.drawable.baseball_ball_solid_blue))
//                .setCompressedImageNoBorder(convertResourceIDToByteArray(R.drawable.baseball_ball_solid))
//                .build()
//        )
//
//        passedIconObj.add( //table tennis
//            SetServerIconRequest.newBuilder()
//            .setPassword(ClientRPC.password)
//            .setPushBack(true)
//            .setIndexNumber(-1)
//            .setIconActive(true)
//            .setCompressedImage(convertResourceIDToByteArray(R.drawable.table_tennis_solid))
//            .setCompressedImageInverted(convertResourceIDToByteArray(R.drawable.table_tennis_solid_blue))
//            .setCompressedImageNoBorder(convertResourceIDToByteArray(R.drawable.table_tennis_solid))
//            .build()
//        )
//
//        passedIconObj.add( //cat
//            SetServerIconRequest.newBuilder()
//                .setPassword(ClientRPC.password)
//                .setPushBack(true)
//                .setIndexNumber(-1)
//                .setIconActive(true)
//                .setCompressedImage(convertResourceIDToByteArray(R.drawable.cat_solid))
//                .setCompressedImageInverted(convertResourceIDToByteArray(R.drawable.cat_solid_blue))
//                .setCompressedImageNoBorder(convertResourceIDToByteArray(R.drawable.cat_solid))
//                .build()
//        )
//
//        passedIconObj.add( //bird
//            SetServerIconRequest.newBuilder()
//                .setPassword(ClientRPC.password)
//                .setPushBack(true)
//                .setIndexNumber(-1)
//                .setIconActive(true)
//                .setCompressedImage(convertResourceIDToByteArray(R.drawable.crow_solid))
//                .setCompressedImageInverted(convertResourceIDToByteArray(R.drawable.crow_solid_blue))
//                .setCompressedImageNoBorder(convertResourceIDToByteArray(R.drawable.crow_solid))
//                .build()
//        )
//
//        passedIconObj.add( //dog
//            SetServerIconRequest.newBuilder()
//                .setPassword(ClientRPC.password)
//                .setPushBack(true)
//                .setIndexNumber(-1)
//                .setIconActive(true)
//                .setCompressedImage(convertResourceIDToByteArray(R.drawable.dog_solid))
//                .setCompressedImageInverted(convertResourceIDToByteArray(R.drawable.dog_solid_blue))
//                .setCompressedImageNoBorder(convertResourceIDToByteArray(R.drawable.dog_solid))
//                .build()
//        )
//
//        passedIconObj.add( //dragon
//            SetServerIconRequest.newBuilder()
//                .setPassword(ClientRPC.password)
//                .setPushBack(true)
//                .setIndexNumber(-1)
//                .setIconActive(true)
//                .setCompressedImage(convertResourceIDToByteArray(R.drawable.dragon_solid))
//                .setCompressedImageInverted(convertResourceIDToByteArray(R.drawable.dragon_solid_blue))
//                .setCompressedImageNoBorder(convertResourceIDToByteArray(R.drawable.dragon_solid))
//                .build()
//        )
//
//        passedIconObj.add( //fish
//            SetServerIconRequest.newBuilder()
//                .setPassword(ClientRPC.password)
//                .setPushBack(true)
//                .setIndexNumber(-1)
//                .setIconActive(true)
//                .setCompressedImage(convertResourceIDToByteArray(R.drawable.fish_solid))
//                .setCompressedImageInverted(convertResourceIDToByteArray(R.drawable.fish_solid_blue))
//                .setCompressedImageNoBorder(convertResourceIDToByteArray(R.drawable.fish_solid))
//                .build()
//        )
//
//        passedIconObj.add( //hippo
//            SetServerIconRequest.newBuilder()
//                .setPassword(ClientRPC.password)
//                .setPushBack(true)
//                .setIndexNumber(-1)
//                .setIconActive(true)
//                .setCompressedImage(convertResourceIDToByteArray(R.drawable.hippo_solid))
//                .setCompressedImageInverted(convertResourceIDToByteArray(R.drawable.hippo_solid_blue))
//                .setCompressedImageNoBorder(convertResourceIDToByteArray(R.drawable.hippo_solid))
//                .build()
//        )
//
//        passedIconObj.add( //painting
//            SetServerIconRequest.newBuilder()
//                .setPassword(ClientRPC.password)
//                .setPushBack(true)
//                .setIndexNumber(-1)
//                .setIconActive(true)
//                .setCompressedImage(convertResourceIDToByteArray(R.drawable.palette_solid))
//                .setCompressedImageInverted(convertResourceIDToByteArray(R.drawable.palette_solid_blue))
//                .setCompressedImageNoBorder(convertResourceIDToByteArray(R.drawable.palette_solid))
//                .build()
//        )