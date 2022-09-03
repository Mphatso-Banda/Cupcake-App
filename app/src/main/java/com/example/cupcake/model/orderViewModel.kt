package com.example.cupcake.model

import android.provider.Settings.Global.getString
import android.util.Log
import android.util.Log.d
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.cupcake.R
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

private const val PRICE_PER_CUPCAKE = 2.00
private const val PRICE_FOR_SAME_DAY_PICKUP =3.00
private const val SPECIAL_ORDER = "Special Flavor"

class OrderViewModel: ViewModel() {

//    set properties
    private val _quantity = MutableLiveData<Int>()
            val quantity: LiveData<Int> get() =  _quantity

    private val _flavor = MutableLiveData<String>()
            val flavor: LiveData<String> = _flavor

    private val _date = MutableLiveData<String>()
            val date: LiveData<String> = _date

    private val _price = MutableLiveData<Double>()
//    The formatted price after LiveData transformation will be a String, here we get the currency
//    The function manipulates the source LiveData and returns an updated value which is also observable
            val price: LiveData<String> = Transformations.map(_price){
                NumberFormat.getCurrencyInstance().format(it)
        }

    var dateOptions = getPickUpOptions()
    //using the init block to initialize the properties when an instance of OrderViewModel is created
    init {
        resetOrder()
    }

//    if special order is selected make the current date not available on pickup options
     fun getUpdatedDate(){
         if (flavor.value.equals(SPECIAL_ORDER)){
             dateOptions = updateDateOptions()
         }else{
             dateOptions = getPickUpOptions()
         }
     }

//    methods to update the properties above, depending on the user's choice:
//    Since these setter methods need to be called from outside the view model, leave them as public
    fun setQuantity(numberCupcakes: Int){
// to access a LiveData object use the value function
        _quantity.value = numberCupcakes
        updatePrice()
    }

    fun setFlavor(desiredFlavor: String){
        _flavor.value = desiredFlavor
//      get updated dates if special flavor is selected
        getUpdatedDate()
    }

    fun setDate(pickupDate: String){
        _date.value = pickupDate
//        to add same day pickup charges call the helper function updatePrice()
        updatePrice()
    }

//    Check whether the flavor has been set or not
    fun hasNoFlavorSet(): Boolean{
        return _flavor.value.isNullOrEmpty()
    }

    private fun updatePrice(){
//    The elvis operator (?:) means if the expression on the left is not null use it
//    Otherwise use the expression on the right (0)
        var calculatePrice = (quantity.value?:0)* PRICE_PER_CUPCAKE
//     check if pickup is same day to add same day pickup price
        if (dateOptions[0] == date.value){
            calculatePrice += PRICE_FOR_SAME_DAY_PICKUP
        }
        _price.value = calculatePrice
    }

//    update the dates if the special flavor is selected. Special flavor will not be available for
//    pickup on  the same day
    private fun updateDateOptions(): List<String>{
        val options = mutableListOf<String>()
//    create a date formatter string, Locale.getDefault() gets the region set on the user's device
        val formatter = SimpleDateFormat("E MMM d", Locale.getDefault())
//    Get a Calendar instance and assign it to a variable. This variable calender contains
//    the current date
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, 1)

        /**
         * Build up a list of dates starting with the current date and the following three dates.
         * Because you'll need 4 date options, repeat this block of code 4 times.
         * This repeat block will format a date, add it to the list of date options,
         * and then increment the calendar by 1 day.*/
        repeat(4){
            options.add(formatter.format(calendar.time))
            calendar.add(Calendar.DATE, 1)
        }
        return options
    }

//    Create and return the list of pick up dates
     fun getPickUpOptions(): List<String>{

        val options = mutableListOf<String>()
//    create a date formatter string, Locale.getDefault() gets the region set on the user's device
        val formatter = SimpleDateFormat("E MMM d", Locale.getDefault())
//    Get a Calendar instance and assign it to a variable. This variable calender contains
//    the current date
        val calendar = Calendar.getInstance()

    /**
     * Build up a list of dates starting with the current date and the following three dates.
     * Because you'll need 4 date options, repeat this block of code 4 times.
     * This repeat block will format a date, add it to the list of date options,
     * and then increment the calendar by 1 day.*/
                repeat(4){
                    options.add(formatter.format(calendar.time))
                    calendar.add(Calendar.DATE, 1)
                }


//   set a default date to the first day
//    setDate(options[0])
    Log.d("orderViewModel", "Ma options {$options}")
        return options
    Log.d("orderViewModel", "Ma options {$options}")
    }

//    reset the MutableLiveData properties in the view model,
//    also sets the default date when initializing values in the init() method
fun resetOrder(){
        _quantity.value = 0
        _flavor.value = ""
        _date.value = dateOptions[0]
        _price.value = 0.0
    }

}