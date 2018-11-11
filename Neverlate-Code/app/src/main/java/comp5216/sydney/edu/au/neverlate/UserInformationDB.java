package comp5216.sydney.edu.au.neverlate;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {UserInformationItem.class}, version = 1, exportSchema = false)
public abstract class UserInformationDB extends RoomDatabase {
    private static final String DATABASE_NAME = "runRecordItem_db";
    private static UserInformationDB DBINSTANCE;

    public abstract UserInformationDao userInfoDao();

    public static UserInformationDB getDatabase(Context context) {
        if (DBINSTANCE == null) {
            synchronized (UserInformationDB.class) {
                DBINSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        UserInformationDB.class, DATABASE_NAME).build();
            }
        }
        return DBINSTANCE;
    }

    public static void destroyInstance() {
        DBINSTANCE = null;
    }
}
