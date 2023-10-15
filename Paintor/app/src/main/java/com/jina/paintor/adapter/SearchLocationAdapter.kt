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
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.jina.paintor.R
import com.jina.paintor.databinding.ItemCalendarDayBinding
import com.jina.paintor.databinding.ItemSearchLocationBinding
import com.jina.paintor.ui.setHighlightText
import com.jina.paintor.utils.TAG
import com.orhanobut.logger.Logger
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException


class SearchLocationAdapter(val context: Context) :
    RecyclerView.Adapter<SearchLocationAdapter.ViewHolder>(), Filterable {
    //    val placesClient = Places.createClient(context)
    private var mResultList = ArrayList<PlaceAutocomplete>()
    private var searchTxt:String = ""

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
        holder.binding.ivFlag.setBackgroundResource(R.drawable.ic_korea_flag)
        holder.binding.tvLocation.setHighlightText(mResultList[position].address.toString(), searchTxt)
        Logger.t(TAG.LOCATION)
            .d("adrress : ${mResultList[position].address}\narea : ${mResultList[position].area}")
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

    private fun getPredictions(constraint: CharSequence): ArrayList<PlaceAutocomplete> {
        val resultList: ArrayList<PlaceAutocomplete> = ArrayList<PlaceAutocomplete>()

        // Create a new token for the autocomplete session. Pass this to FindAutocompletePredictionsRequest,
        // and once again when the user makes a selection (for example when calling fetchPlace()).
        val token = AutocompleteSessionToken.newInstance()

        //https://gist.github.com/graydon/11198540
        // Use the builder to create a FindAutocompletePredictionsRequest.
        val request =
            FindAutocompletePredictionsRequest.builder() // Call either setLocationBias() OR setLocationRestriction().
                //.setLocationBias(bounds)
                //.setCountry("BD")
                //.setTypeFilter(TypeFilter.ADDRESS)
                .setSessionToken(token)
                .setQuery(constraint.toString())
                .build()
        val autocompletePredictions: Task<FindAutocompletePredictionsResponse> =
            Places.createClient(context).findAutocompletePredictions(request)

        // This method should have been called off the main UI thread. Block and wait for at most
        // 60s for a result from the API.
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
            if (findAutocompletePredictionsResponse != null) for (prediction in findAutocompletePredictionsResponse.autocompletePredictions) {
//                Log.i(TAG, prediction.placeId)
                Logger.t(TAG.LOCATION).i("successfull\n$prediction")
                resultList.add(
                    PlaceAutocomplete(
                        prediction.placeId,
                        prediction.getPrimaryText(null).toString(),
                        prediction.getFullText(null).toString()
                    )
                )
            }
            resultList
        } else {
            resultList
        }
    }

    class PlaceAutocomplete internal constructor(
        var placeId: CharSequence,
        var area: CharSequence,
        var address: CharSequence
    ) {
        override fun toString(): String {
            return area.toString()
        }
    }
}