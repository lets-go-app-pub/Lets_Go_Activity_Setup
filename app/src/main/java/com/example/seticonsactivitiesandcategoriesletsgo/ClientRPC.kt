package com.example.seticonsactivitiesandcategoriesletsgo

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import delete_me.DeleteMe.DeleteMeRequest
import delete_me.DeleteMe.DeleteMeResponse
import delete_me.DeleteMeServiceGrpc
import io.grpc.*
import io.grpc.ForwardingClientCall.SimpleForwardingClientCall
import io.grpc.ForwardingClientCallListener.SimpleForwardingClientCallListener
import io.grpc.android.AndroidChannelBuilder
import io.grpc.stub.MetadataUtils
import io.grpc.stub.StreamObserver
import requestfields.RequestFieldsServiceGrpc
import requestfields.ServerIconsRequest
import server_specific.ServerSpecificCommandsServiceGrpc
import server_specific.SetServerActivityOrCategoryRequest
import server_specific.SetServerIconRequest
import server_specific.SetServerValuesResponse
import test_stream.TestStream.CancelTestStreamRequest
import test_stream.TestStream.TestStreamRequest
import test_stream.TestStreamServiceGrpc
import testing_bi_di.TestingBiDi
import testing_bi_di.TestingBiDiServiceGrpc
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class HeaderClientInterceptor(val saveOnCloseResultsToStreamObserver: (status: Status?, trailers: Metadata?)->Unit) : ClientInterceptor {

    override fun <ReqT, RespT> interceptCall(
        method: MethodDescriptor<ReqT, RespT>?,
        callOptions: CallOptions?, next: Channel
    ): ClientCall<ReqT, RespT> {

        return object : SimpleForwardingClientCall<ReqT, RespT>(next.newCall(method, callOptions)) {
            override fun start(responseListener: Listener<RespT>?, headers: Metadata) {
                super.start(object : SimpleForwardingClientCallListener<RespT>(responseListener) {

                    override fun onClose(status: Status?, trailers: Metadata?) {
                        //this seems to be called with onError as well as onCompleted
                        Log.i("deleteMeStream", "onClose called, status: $status trailers: $trailers containsKey: ${trailers?.containsKey(REASON_STREAM_SHUT_DOWN_KEY)}")
                        //the status and trailers are wanted inside of StreamObserver in onCompleted and onError, so this lambda will save them
                        // to it
                        saveOnCloseResultsToStreamObserver(status, trailers)
                        super.onClose(status, trailers)
                    }

                }, headers)
            }
        }
    }

    companion object {

        val REASON_STREAM_SHUT_DOWN_KEY: Metadata.Key<String?> =
            Metadata.Key.of("stream_down_reason", Metadata.ASCII_STRING_MARSHALLER)
    }
}

class ClientRPC {
    var requestObserverDeleteMe: StreamObserver<TestingBiDi.TestingBiDiRequest>? = null
    fun setCategories(passedObj: List<SetServerActivityOrCategoryRequest?>): SetServerValuesResponse? {
        val response = arrayOfNulls<SetServerValuesResponse>(1)
        val client = ServerSpecificCommandsServiceGrpc.newStub(channel)
        val latch = CountDownLatch(1)
        val requestObserver =
            client.setServerCategoryRPC(object : StreamObserver<SetServerValuesResponse?> {
                override fun onNext(value: SetServerValuesResponse?) {
                    response[0] = value
                }

                override fun onError(t: Throwable) {}
                override fun onCompleted() {
                    Log.i("setCategories", "setCategories has completed")
                    latch.countDown()
                }
            })
        for (i in passedObj.indices) {
            requestObserver.onNext(
                passedObj[i]
            )
        }
        requestObserver.onCompleted()
        try {
            latch.await(3L, TimeUnit.SECONDS)
            Log.i("setCategories", "finished")
        } catch (e: InterruptedException) {
            Log.i("setCategories", "Exception" + e.message)
            //e.printStackTrace();
        }
        return response[0]
    }

    fun setActivities(passedObj: List<SetServerActivityOrCategoryRequest?>): SetServerValuesResponse? {
        val response = arrayOfNulls<SetServerValuesResponse>(1)
        val client = ServerSpecificCommandsServiceGrpc.newStub(channel)
        val latch = CountDownLatch(1)
        val requestObserver =
            client.setServerActivityRPC(object : StreamObserver<SetServerValuesResponse?> {
                override fun onNext(value: SetServerValuesResponse?) {
                    response[0] = value
                }

                override fun onError(t: Throwable) {}
                override fun onCompleted() {
                    Log.i("setActivities", "setCategories has completed")
                    latch.countDown()
                }
            })
        for (i in passedObj.indices) {
            requestObserver.onNext(
                passedObj[i]
            )
        }
        requestObserver.onCompleted()
        try {
            latch.await(3L, TimeUnit.SECONDS)
            Log.i("setActivities", "finished")
        } catch (e: InterruptedException) {
            Log.i("setActivities", "Exception" + e.message)
            //e.printStackTrace();
        }
        return response[0]
    }

    fun setIcons(passedObj: List<SetServerIconRequest?>): SetServerValuesResponse? {
        val response = arrayOfNulls<SetServerValuesResponse>(1)
        val client = ServerSpecificCommandsServiceGrpc.newStub(channel)
        val latch = CountDownLatch(1)
        val requestObserver =
            client.setServerIconRPC(object : StreamObserver<SetServerValuesResponse?> {
                override fun onNext(value: SetServerValuesResponse?) {
                    response[0] = value
                }

                override fun onError(t: Throwable) {}
                override fun onCompleted() {
                    Log.i("setIcons", "setCategories has completed")
                    latch.countDown()
                }
            })
        for (i in passedObj.indices) {
            requestObserver.onNext(
                passedObj[i]
            )
        }
        requestObserver.onCompleted()
        try {
            latch.await(3L, TimeUnit.SECONDS)
            Log.i("setIcons", "finished")
        } catch (e: InterruptedException) {
            Log.i("setIcons", "Exception" + e.message)
        }
        return response[0]
    }

    private var idNum = 0
    fun cancelTestStream(): String {
        var message = ""
        val request = CancelTestStreamRequest.newBuilder()
            .build()
        Log.i("testStream", "Starting cancel test stream")
        try {
            val client = TestStreamServiceGrpc.newBlockingStub(channel)
            val response = client.cancelTestStreamRPC(request)
            Log.i("testStream", "Finished cancel test stream")
        } catch (e: StatusRuntimeException) {
            Log.i(
                "testStream", """
     test stream exception of
     ${e.message}
     """.trimIndent()
            )
            message = e.message.toString()
        }
        return message
    }

    fun startTestStream(): String {
        var message = ""
        val request = TestStreamRequest.newBuilder()
            .setId(idNum.toString())
            .build()
        idNum++
        Log.i("testStream", "Starting test stream")
        try {
            val client = TestStreamServiceGrpc.newBlockingStub(channel)
            val response = client
                .testStreamRPC(request)
            while (response.hasNext()) {
                val next = response.next()
                Log.i("testStream", "StreamId: " + next.id + " num: " + next.number)
            }
        } catch (e: StatusRuntimeException) {
            Log.i("testStream", """test stream exception msg${e.message}""".trimIndent())
            Log.i(
                "testStream",
                """test stream exception local msg${e.localizedMessage}""".trimIndent()
            )
            Log.i("testStream", """test stream exception cause${e.cause}""".trimIndent())
            message = e.message.toString()
        }
        Log.i("testStream", "Finishing test stream")
        return message
    }

    private var oid = "60929cdad7bef7dd2eff235c";
    var incrementMe = 0
    private var iDData = Metadata.Key.of("""id""", Metadata.ASCII_STRING_MARSHALLER)
    fun testingStreamBiDi() {

        val responseObserver = object : StreamObserver<TestingBiDi.TestingBiDiResponse> {
            var finalMetaData: Metadata? = null
            var finalStatus: Status? = null

            override fun onNext(value: TestingBiDi.TestingBiDiResponse) {

                when (value.serverResponseCase) {
                    TestingBiDi.TestingBiDiResponse.ServerResponseCase.REQUEST_MESSAGE_INFO_RESPONSE -> {
                        Log.i(
                            "deleteMeStream",
                            "onNext TestingBiDiResponse case: REQUEST_MESSAGE_INFO_RESPONSE, message: ${value.requestMessageInfoResponse.message}"
                        )
                    }
                    TestingBiDi.TestingBiDiResponse.ServerResponseCase.REFRESH_STREAM_RESPONSE -> {
                        Log.i(
                            "deleteMeStream",
                            "onNext TestingBiDiResponse case: REFRESH_STREAM_RESPONSE, expiration_time: ${value.refreshStreamResponse.chatStreamExpirationTimestampInMillis}"
                        )
                    }
                    TestingBiDi.TestingBiDiResponse.ServerResponseCase.RETURN_MESSAGE_FROM_INJECTION -> {
                        Log.i(
                            "deleteMeStream",
                            "onNext TestingBiDiResponse case: RETURN_MESSAGE_FROM_INJECTION, message: ${value.returnMessageFromInjection.message}"
                        )
                    }
                    TestingBiDi.TestingBiDiResponse.ServerResponseCase.SERVERRESPONSE_NOT_SET,
                    null -> {
                        Log.i(
                            "deleteMeStream",
                            "onNext TestingBiDiResponse case: ${value.serverResponseCase}"
                        )
                    }
                }
            }

            override fun onError(t: Throwable) {
                Log.i("deleteMeStream", "onError: " + t.message)
            }

            override fun onCompleted() {
                Log.i("deleteMeStream", "onCompleted called, status: $finalStatus trailers: $finalMetaData containsKey: ${finalMetaData?.containsKey(
                    HeaderClientInterceptor.REASON_STREAM_SHUT_DOWN_KEY
                )}")
                Context.current()
            }
        }

        val metadata = Metadata()

        metadata.put(
            iDData, oid
        )

        val interceptor = HeaderClientInterceptor { status, trailers ->
            responseObserver.finalStatus = status
            responseObserver.finalMetaData = trailers
        }

        val newChannel = ClientInterceptors.intercept(channel, interceptor)

        val myStub = TestingBiDiServiceGrpc.newStub(newChannel)

        val asyncClient = MetadataUtils.attachHeaders(
            myStub,
            metadata
        )

        requestObserverDeleteMe =
            asyncClient.testingBiDiRPC(responseObserver)
    }

    fun deleteMeUnary() {
        val client = DeleteMeServiceGrpc.newBlockingStub(channel)

        try {
            val response =
                client.testingRPC(DeleteMeRequest.newBuilder().setDeleteMeRequest(456).build())

            Log.i("deleteMeStream", "received: " + response.deleteMeResponse)
        } catch (e: StatusRuntimeException) {
            Log.i("deleteMeStream", "received error: ${e.status} message: ${e.message}")
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    fun deleteMeServer() {
        val asyncClient = DeleteMeServiceGrpc.newBlockingStub(channel)
        val latch = CountDownLatch(1)
        val requestObserver2 = asyncClient.deleteMeServerRPC(
            DeleteMeRequest.newBuilder().setDeleteMeRequest(333).build()
        )
        requestObserver2.forEachRemaining { thingy: DeleteMeResponse ->
            Log.i(
                "deleteMeStream",
                "received: " + thingy.deleteMeResponse
            )
        }
        try {
            latch.await()
        } catch (ignored: InterruptedException) {
        }
    }

    fun deleteMeClient() {
        val asyncClient = DeleteMeServiceGrpc.newStub(channel)
        val latch = CountDownLatch(1)
        val requestObserver2 =
            asyncClient.deleteMeClientRPC(object : StreamObserver<DeleteMeResponse> {
                override fun onNext(value: DeleteMeResponse) {
                    Log.i("deleteMeStream", "onNext value: " + value.deleteMeResponse)
                }

                override fun onError(t: Throwable) {
                    Log.i("deleteMeStream", "onError: " + t.message)
                    latch.countDown()
                }

                override fun onCompleted() {
                    Log.i("deleteMeStream", "onCompleted")
                    latch.countDown()
                }
            })
        for (i in 0..4) {
            requestObserver2.onNext(DeleteMeRequest.newBuilder().setDeleteMeRequest(i).build())
        }
        requestObserver2.onCompleted()
        try {
            latch.await()
        } catch (ignored: InterruptedException) {
        }
    }

    fun geIconsFunc() {
        val request = ServerIconsRequest.newBuilder()
            .setLetsGoVersion(1.0)
            .setLoggedInToken("5f299fd77250000071004da8")
            .addIconIndex(0)
            .build()

        //the 'blocking' stubs called from a Kotlin coRoutine only seem to block the coRoutine itself not the entire thread
        val client = RequestFieldsServiceGrpc.newBlockingStub(channel)
        val responseIterator = client.requestServerIconsRPC(request)
        var theIndex = 0
        while (responseIterator.hasNext()) {
            theIndex++
        }
    }

    companion object {
        //NOTE: the way that the channel builder implements its 'client load balancing' does it for each
        // call, instead of finding a server, waiting a while, then re-balancing, so the only way I think
        // is to make a channel for each server
        // also shutdown(). allows the running stream to continue
        var password = "abv"
        var channel = AndroidChannelBuilder.forAddress(
            "10.0.2.2",
            50051
        ) //static ManagedChannel channel = AndroidChannelBuilder.forAddress("{redacated}", 50051)
            .usePlaintext()
            .build()
    }
}
