package cfwz.skiti.go4lunch.api;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;

import cfwz.skiti.go4lunch.models.Workmate;

import static com.firebase.ui.auth.ui.email.EmailLinkFragment.TAG;

public class UserHelper {

        private static final String COLLECTION_NAME = "Workmates";

        // --- COLLECTION REFERENCE ---

        public static CollectionReference getWorkmatesCollection(){
            return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
        }

        // --- CREATE ---

        public static Task<Void> createWorkmate(String uid, @Nullable String urlPicture, String name) {
            Workmate workmateToCreate = new Workmate(uid,urlPicture, name);
            return UserHelper.getWorkmatesCollection().document(uid).set(workmateToCreate);
        }

        // --- GET ---

        public static Task<DocumentSnapshot> getWorkmate(String uid){
            return UserHelper.getWorkmatesCollection().document(uid).get(); }


        // --- UPDATE ---

        public static Task<Void> updateRestaurant(String restaurant, String uid) {
            return UserHelper.getWorkmatesCollection().document(uid).update("restaurant", restaurant);
        }

        public static Task<Void> updateFavorite(ArrayList<String>favorite, String uid){
            return UserHelper.getWorkmatesCollection().document(uid).update("favorite",favorite);
        }

}
