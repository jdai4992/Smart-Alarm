package comp5216.sydney.edu.au.neverlate;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface UserInformationDao {
    @Query("SELECT * FROM userInformationRecord")
    List<UserInformationItem> listAll();

    @Insert
    void insert(UserInformationItem userInformationItem);

    @Insert
    void insertAll(UserInformationItem... userInformationItems);

    @Query("DELETE FROM userInformationRecord")
    void deleteAll();
}
