import com.teamtriad.forpets.UserData
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("users.json")
    fun getLoginData(): Call<UserData>
}