package com.net.pvr1.ui.bookingSession.response

import java.io.Serializable


data class BookingResponse(
    val code: Int,
    val minversion: Any,
    val msg: String,
    val output: Output,
    val result: String,
    val version: Any
) : Serializable {
    data class Output(
        val adlt: Boolean,
        val btnc: String,
        val ca: String,
        val cd: String,
        val cert: String,
        val cinemas: ArrayList<Cinema>,
        val ct: ArrayList<String>,
        val d: String,
        val dys: ArrayList<Dy>,
        val gnr: String,
        val icn: ArrayList<String>,
        val img: List<String>,
        val lc: String,
        val len: String,
        val lng: String,
        val lngs: ArrayList<String>,
        val mb: Mb,
        val mid: String,
        val mih: String,
        val miv: String,
        val nm: String,
        val od: String,
        val p: String,
        val ph: ArrayList<Ph>,
        val prs: Prs,
        val pu: ArrayList<Pu>,
        val rt: String,
        val sm: String,
        val sps: ArrayList<SPS>,
        val su: String,
        val tag: String,
        val ul: Boolean,
        val vdo: String,
        val wib: String
    ) : Serializable {
        data class SPS(
            val id: String,
            val na: String,
            val txt: String
        ):Serializable
        data class Ph(
            val buttonText: String,
            val cities: String,
            val i: String,
            val id: Int,
            val location: String,
            val name: String,
            val platform: String,
            val priority: Int,
            val redirectView: String,
            val redirect_url: String,
            val screen: String,
            val text: String,
            val trailerUrl: String,
            val type: String
        ):Serializable
        data class Pu(
            val buttonText: String,
            val cities: String,
            val i: String,
            val id: Int,
            val location: String,
            val name: String,
            val platform: String,
            val priority: Int,
            val redirectView: String,
            val redirect_url: String,
            val screen: String,
            val text: String,
            val trailerUrl: String,
            val type: String
        ):Serializable
        data class Cinema(
            val add: String,
            val cf: Boolean,
            val childs: ArrayList<Child>,
            val cid: Int,
            val cn: String,
            val dst: Int,
            val hc: Boolean,
            val ina: String,
            val lc: String,
            val lg: String,
            val lt: String,
            val mc: Boolean,
            val mdc: Boolean,
            val mds: Boolean,
            val mdt: Any,
            val oua: String,
            val sc: Int
        ) : Serializable {
            data class Child(
                val at: String,
                val ccid: String,
                val ccn: String,
                val csc: Int,
                val ct: String,
                val hc: Boolean,
                val i: String,
                val mdc: Boolean,
                val mds: Boolean,
                val mdt: Any,
                val sws: List<Sw>
            ) : Serializable {
                data class Sw(
                    val lk: String,
                    val lng: String,
                    val s: ArrayList<S>?,
                    val tx: Any
                ) : Serializable {
                    data class S(
                        val adl: Boolean,
                        val `as`: Int,
                        val at: String,
                        val ba: Boolean,
                        val cc: String,
                        val comm: String,
                        val et: String,
                        val hc: Boolean,
                        val mc: String,
                        val mdc: Boolean,
                        val mds: Boolean,
                        val mdt: Any,
                        val mn: Any,
                        val pg: String,
                        val prs: ArrayList<Pr>,
                        val sd: Long,
                        val sh: String,
                        val sid: Int,
                        val sn: Int,
                        val ss: Int,
                        val st: String,
                        val ts: Int,
                        val txt: String
                    ):Serializable {
                        data class Pr(
                            val ar: String,
                            val `as`: Int,
                            val bp: String,
                            val bv: String,
                            val n: String,
                            val p: String,
                            val st: Int,
                            val ts: Int,
                            val tt: String
                        ) : Serializable
                    }
                }
            }
        }

        data class Mb(
            val akas: List<Any>,
            val alternateTitles: List<String>,
            val alternateUrls: List<String>,
            val apiPath: String,
            val cast: List<Cast>,
            val certifications: Certifications,
            val connections: List<Connection>,
            val crew: List<Crew>,
            val criticReviews: List<CriticReview>,
            val featured: Boolean,
            val filmType: String,
            val filmingEndDate: String,
            val filmingLocations: List<FilmingLocation>,
            val filmingStartDate: String,
            val genres: List<String>,
            val globalReleaseDate: String,
            val goofs: List<Any>,
            val keywords: List<Any>,
            val language: String,
            val languageData: LanguageDataX,
            val links: List<Any>,
            val localName: String,
            val mergedTo: Any,
            val movieRating: MovieRating,
            val moviebuffUrl: String,
            val musicLabels: List<String>,
            val musicRating: MusicRating,
            val name: String,
            val news: List<New>,
            val parent: Any,
            val poster: String,
            val posters: List<Any>,
            val primaryCast: List<PrimaryCast>,
            val primaryCrew: List<PrimaryCrew>,
            val purchaseLinks: List<Any>,
            val releaseDates: ReleaseTypes,
            val releaseStatuses: ReleaseStatuses,
            val releaseTypes: ReleaseTypes,
            val runningTime: Int,
            val shortSynopsis: Any,
            val status: String,
            val stills: List<Any>,
            val storyline: String,
            val synopsis: String,
            val taglines: List<Any>,
            val techDetails: List<TechDetail>,
            val thirdPartyIdentifiers: List<Any>,
            val tracks: List<Track>,
            val trailers: Trailers,
            val trivia: List<String>,
            val triviaData: List<TriviaData>,
            val type: String,
            val updatedAt: Any,
            val url: String,
            val uuid: String,
            val videos: List<VideoX>
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
                val movieRating: MovieRating,
                val moviebuffUrl: String,
                val name: String,
                val poster: String,
                val releaseDates: ReleaseDates,
                val releaseTypes: ReleaseTypes,
                val type: String,
                val url: String,
                val uuid: String
            )

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

            data class CriticReview(
                val createdAt: String,
                val rating: String,
                val resource: Resource,
                val reviewKind: String,
                val reviewType: String,
                val reviewer: Reviewer,
                val source: Source,
                val summary: String,
                val url: String,
                val uuid: String,
                val votesCount: Int
            ) : Serializable {
                data class Reviewer(
                    val name: String,
                    val uuid: String
                ) : Serializable

                data class Source(
                    val name: String,
                    val url: String,
                    val uuid: String
                ) : Serializable

                data class Resource(
                    val uuid: String
                ) : Serializable
            }

            data class FilmingLocation(
                val parent: String,
                val parentType: String,
                val parentUuid: String,
                val text: String
            ) : Serializable

            data class LanguageData(
                val name: String,
                val uuid: String
            ) : Serializable

            data class LanguageDataX(
                val name: String,
                val uuid: String
            ) : Serializable

            data class MusicRating(
                val count: Int,
                val value: String
            ) : Serializable

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
            ) : Serializable

            data class TechDetail(
                val `data`: List<String>,
                val name: String
            ) : Serializable

            data class New(
                val date: String,
                val poster: String,
                val source: SourceX,
                val summary: String,
                val url: String,
                val writer: String
            ) : Serializable {
                data class SourceX(
                    val name: String,
                    val uuid: String
                )
            }

            data class ReleaseTypes(
                val `in`: String
            ) : Serializable

            data class MovieRating(
                val count: Int,
                val value: String
            ) : Serializable

            data class ReleaseStatuses(
                val ae: Any
            ) : Serializable

            data class Track(
                val display: String,
                val duration: Int,
                val name: String,
                val number: Int,
                val purchaseLinks: List<Any>,
                val roles: List<Crew.Role>,
                val video: Video
            ) : Serializable {
                data class Video(
                    val caption: String,
                    val embedUrl: String,
                    val featured: Boolean,
                    val key: String,
                    val thumbnail: String,
                    val type: String,
                    val url: String
                ) : Serializable
            }

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

            data class TriviaData(
                val createdAt: String,
                val text: String
            ) : Serializable

            data class VideoX(
                val caption: String,
                val createdAt: String,
                val embedUrl: String,
                val featured: Boolean,
                val key: String,
                val thumbnail: String,
                val type: String,
                val url: String
            ) : Serializable

            data class ReleaseDates(
                val `in`: String
            ) : Serializable
        }

        data class Dy(
            val d: String,
            val dt: String,
            val showdate: Long,
            val wd: String,
            val wdf: String
        ) : Serializable

        data class Prs(
            val max: String,
            val min: String
        ) : Serializable
    }
}