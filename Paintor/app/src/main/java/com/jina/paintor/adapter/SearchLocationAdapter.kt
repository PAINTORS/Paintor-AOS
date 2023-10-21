package com.jina.paintor.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.jina.paintor.data.Location
import com.jina.paintor.databinding.ItemSearchLocationBinding
import com.jina.paintor.ui.setCountryFlag
import com.jina.paintor.ui.setHighlightText
import com.jina.paintor.utils.TAG
import com.orhanobut.logger.Logger
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException


class SearchLocationAdapter(val context: Context) :
    RecyclerView.Adapter<SearchLocationAdapter.ViewHolder>(), Filterable {
    private var mResultList = ArrayList<Location>()
    private var searchTxt: String = ""

    inner class ViewHolder(val binding: ItemSearchLocationBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchLocationAdapter.ViewHolder {
        return ViewHolder(
            ItemSearchLocationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SearchLocationAdapter.ViewHolder, position: Int) {
        // NOTE : 자..잠깐... 국가가 195개나 된다고..?ㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋ....
        // https://opendata.mofa.go.kr/lod/countryInfo.do
        holder.binding.ivFlag.setCountryFlag(mResultList[position].countryName.toString())
        holder.binding.tvLocation.setHighlightText(
            mResultList[position].fullName.toString(),
            searchTxt
        )
    }

    override fun getItemCount(): Int {
        return mResultList.size
    }

    override fun getFilter(): Filter? {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val results = FilterResults()
                // Skip the autocomplete query if no constraints are given.
                if (constraint != null) {
                    // Query the autocomplete API for the (constraint) search string.
                    mResultList = getPredictions(constraint)
                    if (mResultList != null) {
                        // The API successfully returned results.
                        searchTxt = constraint.toString()
                        results.values = mResultList
                        results.count = mResultList.size
                    }
                }
                return results
            }

            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                if (results != null && results.count > 0) {
                    // The API returned at least one result, update the data.
                    notifyDataSetChanged()
                } else {
                    // The API did not return any results, invalidate the data set.
                    //notifyDataSetInvalidated();
                }
            }
        }
    }

    private fun getPredictions(constraint: CharSequence): ArrayList<Location> {
        val resultList: ArrayList<Location> = ArrayList<Location>()
        val token = AutocompleteSessionToken.newInstance()
        val request = FindAutocompletePredictionsRequest.builder()
            .setTypeFilter(TypeFilter.CITIES)
            .setSessionToken(token)
            .setQuery(constraint.toString())
            .build()
        val autocompletePredictions: Task<FindAutocompletePredictionsResponse> =
            Places.createClient(context).findAutocompletePredictions(request)
        try {
            Tasks.await<FindAutocompletePredictionsResponse>(
                autocompletePredictions,
                60,
                TimeUnit.SECONDS
            )
        } catch (e: ExecutionException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: TimeoutException) {
            e.printStackTrace()
        }
        return if (autocompletePredictions.isSuccessful) {
            val findAutocompletePredictionsResponse = autocompletePredictions.result
            if (findAutocompletePredictionsResponse != null) {
                for (prediction in findAutocompletePredictionsResponse.autocompletePredictions) {
                    Logger.t(TAG.LOCATION).i(
                        "getPrimaryText : ${prediction.getPrimaryText(null).toString()}\n" +
                                "getSecondaryText : ${
                                    prediction.getSecondaryText(null).toString()
                                }\n" +
                                "getFullText : ${prediction.getFullText(null).toString()}"
                    )
                    resultList.add(
                        Location(
                            prediction.placeId,
                            prediction.getPrimaryText(null).toString(), // city
                            prediction.getSecondaryText(null).toString(), // country
                            prediction.getFullText(null).toString() // full
                        )
                    )
                }
            }
            resultList
        } else {
            resultList
        }
    }
}