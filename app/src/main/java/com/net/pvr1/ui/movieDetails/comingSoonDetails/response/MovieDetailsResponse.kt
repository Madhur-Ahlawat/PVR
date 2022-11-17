package com.net.pvr1.ui.movieDetails.commingSoon.response

import java.io.Serializable

data class MovieDetailsResponse(
    val code: Int,
    val minversion: Any,
    val msg: String,
    val output: Output,
    val result: String,
    val version: Any
) : Serializable {
    data class Output(
        val c: String,
        val ca: String,
        val d: String,
        val genre: String,
        val i: List<String>,
        val icons: List<String>,
        val id: String,
        val imdb: String,
        val l: String,
        val lc: String,
        val lng: String,
        val lngs: List<String>,
        val mb: Mb,
        val mih: String,
        val miv: String,
        val mopeningdate: String,
        val n: String,
        val ofb: String,
        val offr: Offr,
        val othergenres: String,
        val otherlanguages: String,
        val p: String,
        val ph: List<Any>,
        val pu: List<Any>,
        val rt: String,
        val rtt: String,
        val sapi: String,
        val sm: String,
        val su: String,
        val t: String,
        val tag: String,
        val trs: List<Trs>,
        val ul: Boolean,
        val wib: String,
        val wit: String
    ) : Serializable

    data class Offr(
        val cd: Any,
        val end: Any,
        val from: Any,
        val i: Any,
        val mc: Any,
        val mn: Any,
        val tc: Any,
        val ty: Any,
        val v: Any
    ) : Serializable

    data class Mb(
        val akas: List<Any>,
        val alternateTitles: List<Any>,
        val alternateUrls: List<String>,
        val apiPath: String,
        val cast: List<Cast>,
        val certifications: Certifications,
        val connections: List<Connection>,
        val crew: List<Crew>,
        val criticReviews: List<Any>,
        val featured: Boolean,
        val filmType: String,
        val filmingEndDate: String,
        val filmingLocations: List<Any>,
        val filmingStartDate: String,
        val genres: List<String>,
        val globalReleaseDate: String,
        val goofs: List<Any>,
        val keywords: List<Any>,
        val language: String,
        val languageData: LanguageData,
        val links: List<Any>,
        val localName: String,
        val mergedTo: Any,
        val movieRating: MovieRating,
        val moviebuffUrl: String,
        val musicLabels: List<Any>,
        val musicRating: MusicRating,
        val name: String,
        val news: List<Any>,
        val parent: Any,
        val poster: String,
        val posters: List<Any>,
        val primaryCast: List<PrimaryCast>,
        val primaryCrew: List<PrimaryCrew>,
        val purchaseLinks: List<Any>,
        val releaseDates: ReleaseDates,
        val releaseStatuses: ReleaseStatuses,
        val releaseTypes: ReleaseTypes,
        val runningTime: Double,
        val shortSynopsis: Any,
        val status: String,
        val stills: List<Any>,
        val storyline: String,
        val synopsis: String,
        val taglines: List<Any>,
        val techDetails: List<TechDetail>,
        val thirdPartyIdentifiers: List<Any>,
        val tracks: List<Tracks>,
        val trailers: Trailers,
        val trivia: List<Any>,
        val triviaData: List<Any>,
        val type: String,
        val updatedAt: Any,
        val url: String,
        val uuid: String,
        val videos: List<Video>
    ) : Serializable {
        data class Cast(
            val apiPath: String,
            val character: String,
            val department: String,
            val moviebuffUrl: String,
            val name: String,
            val poster: String,
            val primary: Boolean,
            val role: String,
            val type: String,
            val url: String,
            val uuid: String
        ) : Serializable

        data class Certifications(
            val `in`: String
        ) : Serializable

        data class Connection(
            val apiPath: String,
            val certifications: Certifications,
            val connectionType: String,
            val language: String,
            val languageData: LanguageData,
            val movieRating: MusicRating,
            val moviebuffUrl: String,
            val name: String,
            val poster: String,
            val releaseDates: ReleaseDates,
            val releaseTypes: ReleaseTypes,
            val type: String,
            val url: String,
            val uuid: String
        ) : Serializable

        data class LanguageData(
            val name: String,
            val uuid: String
        ) : Serializable

        data class MovieRating(
            val count: Int,
            val value: String
        ) : Serializable

        data class MusicRating(
            val count: Int,
            val value: String
        ) : Serializable

        data class TechDetail(
            val `data`: List<String>,
            val name: String
        ) : Serializable

        data class Tracks(
            val number: String,
            val name: String,
            val display: String,
            val duration: Boolean,
            val roles: List<Crew.Role>,
            val purchaseLinks: String,
            val video: String,) : Serializable

        data class Trailers(
            val caption: String,
            val createdAt: String,
            val embedUrl: String,
            val featured: Boolean,
            val key: String,
            val thumbnail: String,
            val type: String,
            val url: String
        ) : Serializable

        data class Video(
            val caption: String,
            val createdAt: String,
            val embedUrl: String,
            val featured: Boolean,
            val key: String,
            val thumbnail: String,
            val type: String,
            val url: String
        ) : Serializable

        data class ReleaseStatuses(
            val ae: Any
        ) : Serializable

        data class ReleaseTypes(
            val `in`: String
        ) : Serializable

        data class ReleaseDates(
            val `in`: String
        ) : Serializable

        data class Crew(
            val department: String,
            val roles: List<Role>
        ) : Serializable {
            data class Role(
                val apiPath: String,
                val character: String,
                val department: String,
                val moviebuffUrl: String,
                val name: String,
                val poster: String,
                val primary: Boolean,
                val role: String,
                val type: String,
                val url: String,
                val uuid: String
            ) : Serializable
        }

        data class PrimaryCast(
            val apiPath: String,
            val character: String,
            val department: String,
            val moviebuffUrl: String,
            val name: String,
            val poster: String,
            val primary: Boolean,
            val role: String,
            val type: String,
            val url: String,
            val uuid: String
        ) : Serializable

        data class PrimaryCrew(
            val apiPath: String,
            val character: String,
            val department: String,
            val moviebuffUrl: String,
            val name: String,
            val poster: String,
            val primary: Boolean,
            val role: String,
            val type: String,
            val url: String,
            val uuid: String
        ):Serializable

    }

    data class Trs(
        val id: String,
        val t: String,
        val d: String,
        val u: String,
        val ty: String
    ):Serializable


}