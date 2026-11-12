package com.fourleafclover.tarot.network


import com.fourleafclover.tarot.data.DemoConfigDto
import com.fourleafclover.tarot.data.MatchTarotInputDto
import com.fourleafclover.tarot.data.TarotIdsInputDto
import com.fourleafclover.tarot.data.TarotInputDto
import com.fourleafclover.tarot.data.TarotOutputDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface TarotService {
    @POST("tarot/{path}")
    fun postTarotResult(@Body tarotInputDto: TarotInputDto, @Path("path") path: String): Call<TarotOutputDto>

    @POST("tarot/my")
    fun getMyTarotResult(@Body tarotIds: TarotIdsInputDto): Call<ArrayList<TarotOutputDto>>

    @POST("tarot/match")
    fun getMatchResult(@Body matchTarotRequest: MatchTarotInputDto): Call<TarotOutputDto>

    @GET("config/demo")
    fun getDemoConfig(): Call<DemoConfigDto>
}