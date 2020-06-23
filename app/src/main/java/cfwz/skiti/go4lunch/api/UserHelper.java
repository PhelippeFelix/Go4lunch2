package cfwz.skiti.go4lunch.api;

import androidx.annotation.Nullable;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import cfwz.skiti.go4lunch.model.Workmate;


public class UserHelper {
        private static final String COLLECTION_NAME = "Workmates";

        // --- COLLECTION REFERENCE ---

        public static CollectionReference getWorkmatesCollection(){
            return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
        }

        // --- CREATE ---

        public static Task<Void> createWorkmate(String uid, @Nullable String urlPicture, String name) {
            if (urlPicture ==null)
                urlPicture = "https://revuelespritlibre.org/sites/revuelespritlibre.org/files/portraits/userportrait.jpg";
            Workmate workmateToCreate = new Workmate(uid,urlPicture, name);
            return UserHelper.getWorkmatesCollection().document(uid).set(workmateToCreate);
        }

        // --- GET ---

        public static Task<DocumentSnapshot> getWorkmate(String uid){
            return UserHelper.getWorkmatesCollection().document(uid).get(); }

        // --- UPDATE ---

    public static Task<Void> updateUserSettings(String userId,boolean notification){
        return  UserHelper.getWorkmatesCollection().document(userId).update("notification",notification);
    }
}
