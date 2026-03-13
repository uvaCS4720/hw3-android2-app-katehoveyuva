package edu.nd.pmcburne.hwapp.one.data

import edu.nd.pmcburne.hwapp.one.model.ScoreboardResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface NcaaApiService {

    @GET("scoreboard/basketball-{gender}/d1/{year}/{month}/{day}")
    suspend fun getScores(
        @Path("gender") gender: String, // "men" or "women"
        @Path("year") year: String,     // "2026"
        @Path("month") month: String,   // "02"
        @Path("day") day: String        // "17"
    ): ScoreboardResponse
}