import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.cupcake.model.OrderViewModel
import junit.framework.TestCase.assertEquals
import org.junit.Rule
import org.junit.Test

class ViewModelTests {


//    the UI runs on the main thread and LiveData objects cannot access the main thread.
//    We have to explicitly state that LiveData objects should not call the main thread anytime we
//    test LiveData with the rule below
@get:Rule
var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun quantity_twelve_pancakes(){
        val viewModel = OrderViewModel()
//        when testing objects in LiveData the objects need to be observed in order for changes to be emitted
        viewModel.quantity.observeForever{}
        viewModel.setQuantity(12)
        assertEquals(12, viewModel.quantity.value)

    }
    @Test
    fun price_twelve_pancakes(){
        val viewModel = OrderViewModel()
        viewModel.price.observeForever {}
        viewModel.setQuantity(12)
        assertEquals("$27.00", viewModel.price.value)
    }
}