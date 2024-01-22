import com.teamtriad.forpets.UserData
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("Users.json")
    fun getAllUserData(): Call<Map<String, UserData>>
}