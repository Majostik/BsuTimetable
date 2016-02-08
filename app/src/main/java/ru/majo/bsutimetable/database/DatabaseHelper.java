package ru.majo.bsutimetable.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Majo on 31.01.2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "stud.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_AUDITORY = "rasp_auditory";

    public static final String KOR_AUDITORY = "kor";
    public static final String AUD_AUDITORY = "aud";

    public static final String TABLE_DMAIN = "rasp_dmain";

    public static final String GRUP_DMAIN = "grup";
    public static final String DZ_DMAIN = "dz";
    public static final String PARA_DMAIN = "para";
    public static final String DIS_DMAIN = "dis";
    public static final String VID_DMAIN = "vid";
    public static final String AUD_DMAIN = "aud";
    public static final String PREP_DMAIN = "prep";
    public static final String TIP_DMAIN = "tip";

    public static final String TABLE_GROUP = "rasp_group";

    public static final String GRUP_GROUP = "grup";
    public static final String TIP_GROUP = "tip";

    public static final String TABLE_MAIN = "rasp_main";

    public static final String SUBJECT_ID_MAIN = "subject_id";
    public static final String TEACHER_ID_MAIN = "teacher_id";
    public static final String AUDITORY_MAIN = "auditory";
    public static final String PAIR_TYPE_MAIN = "pair_type";
    public static final String PAIR_NUMBER_MAIN = "pair_number";
    public static final String GROUP_NUMBER_MAIN = "group_number";
    public static final String DAY_OF_WEEK_MAIN = "day_of_week";
    public static final String WEEK_MAIN = "week";

    public static final String TABLE_SUBJECTS = "rasp_subjects";

    public static final String DIS_SUBJECTS = "dis";
    public static final String DISS_SUBJECTS = "diss";
    public static final String DISP_SUBJECTS = "disp";

    public static final String TABLE_TEACHERS = "rasp_teachers";

    public static final String PREP_TEACHERS = "prep";
    public static final String FIO_TEACHERS = "fio";


    public static final String TABLE_SAVED = "table_saved";

    public static final String SAVED_INPUT = "input";
    public static final String SAVED_TYPE = "type";

    private static final String CREATE_TABLE_AUDITORY = "CREATE TABLE " + TABLE_AUDITORY + "( "
            + KOR_AUDITORY + " VARCHAR(3), " + AUD_AUDITORY + " VARCHAR(4));";

    private static final String CREATE_TABLE_DMAIN = "CREATE TABLE "
            + TABLE_DMAIN + "( " + GRUP_DMAIN + " VARCHAR(42), " + DZ_DMAIN + " DATE, "
            + PARA_DMAIN + " VARCHAR(1)," + DIS_DMAIN + " VARCHAR(11), " + VID_DMAIN +
            " VARCHAR(2), " + AUD_DMAIN + " VARCHAR(4), "
            + PREP_DMAIN + " VARCHAR(11), " + TIP_DMAIN + " VARCHAR(5));";

    public static final String CREATE_TABLE_GROUP = "CREATE TABLE " + TABLE_GROUP + "( "
            + GRUP_GROUP + " VARCHAR(6)," + TIP_GROUP + " VARCHAR(1));";

    public static final String CREATE_TABLE_MAIN = "CREATE TABLE " + TABLE_MAIN + "( "
            + SUBJECT_ID_MAIN + " VARCHAR(11), " + TEACHER_ID_MAIN + " VARCHAR(11), " + AUDITORY_MAIN +
            " VARCHAR(4), " + PAIR_TYPE_MAIN + " VARCHAR(10), " + PAIR_NUMBER_MAIN + " VARCHAR(6), "
            + GROUP_NUMBER_MAIN + " VARCHAR(6), " + DAY_OF_WEEK_MAIN + " VARCHAR(6), " + WEEK_MAIN +
            " VARCHAR(6));";

    public static final String CREATE_TABLE_SUBJECTS = "CREATE TABLE " + TABLE_SUBJECTS + "( "
            + DIS_SUBJECTS + " VARCHAR(11), " + DISS_SUBJECTS + " VARCHAR(16), " + DISP_SUBJECTS + " VARCHAR(68));";

    public static final String CREATE_TABLE_TEACHERS = "CREATE TABLE " + TABLE_TEACHERS + "( "
            + PREP_TEACHERS + " VARCHAR(11), " + FIO_TEACHERS + " VARCHAR(16));";

    public static final String CREATE_TABLE_SAVED = "CREATE TABLE " + TABLE_SAVED + "( " + SAVED_INPUT +
            " INTEGER," + SAVED_TYPE + " VARCHAR(30));";

    public DatabaseHelper(Context context) {
        // TODO Auto-generated constructor stub
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase arg0) {
        arg0.execSQL(CREATE_TABLE_AUDITORY);
        arg0.execSQL(CREATE_TABLE_DMAIN);
        arg0.execSQL(CREATE_TABLE_GROUP);
        arg0.execSQL(CREATE_TABLE_MAIN);
        arg0.execSQL(CREATE_TABLE_SUBJECTS);
        arg0.execSQL(CREATE_TABLE_TEACHERS);
        arg0.execSQL(CREATE_TABLE_SAVED);


        arg0.execSQL("CREATE INDEX idx_auditory ON " + TABLE_AUDITORY + "(" + AUD_AUDITORY + ")");
        arg0.execSQL("CREATE INDEX idx_dmain ON " + TABLE_DMAIN + "(" + GRUP_DMAIN + ")");
        arg0.execSQL("CREATE INDEX idx_group ON " + TABLE_GROUP + "(" + GRUP_GROUP + ")");
        arg0.execSQL("CREATE INDEX idx_main ON " + TABLE_MAIN + "(" + GROUP_NUMBER_MAIN + ")");
        arg0.execSQL("CREATE INDEX idx_subjects ON " + TABLE_SUBJECTS + "(" + DIS_SUBJECTS + ")");
        arg0.execSQL("CREATE INDEX idx_teachers ON " + TABLE_TEACHERS + "(" + FIO_TEACHERS + ")");

    }

    public void clearDB(SQLiteDatabase arg0) {
        arg0.execSQL("DELETE FROM " + TABLE_MAIN);
        arg0.execSQL("DELETE FROM " + TABLE_SUBJECTS);
        arg0.execSQL("DELETE FROM " + TABLE_TEACHERS);
        arg0.execSQL("DELETE FROM " + TABLE_DMAIN);
        arg0.execSQL("DELETE FROM " + TABLE_GROUP);
        arg0.execSQL("DELETE FROM " + TABLE_AUDITORY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // TODO Aut/o-generated method stub
        arg0.execSQL("DROP TABLE IF EXISTS " + TABLE_MAIN);
        arg0.execSQL("DROP TABLE IF EXISTS " + TABLE_SUBJECTS);
        arg0.execSQL("DROP TABLE IF EXISTS " + TABLE_TEACHERS);
        arg0.execSQL("DROP TABLE IF EXISTS " + TABLE_AUDITORY);
        arg0.execSQL("DROP TABLE IF EXISTS " + TABLE_DMAIN);
        arg0.execSQL("DROP TABLE IF EXISTS " + TABLE_GROUP);
        onCreate(arg0);
    }
}
