package invest.com.swapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import invest.com.swapp.repository.FundamentalRow
import invest.com.swapp.repository.FundamentalsRepository

class FundamentalModel(application: Application):AndroidViewModel(application) {

    val funRow: MutableLiveData<List<FundamentalRow>> = MutableLiveData()
    var repository: FundamentalsRepository = FundamentalsRepository()

    suspend fun getFundamentals(cpid: Int){
       var result = repository.retrieveFinancial(cpid)
       funRow.postValue(result)
    }
}