package voterSearch.app.SmdInfo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "MajrekarDB";
	private static final int DATABASE_VERSION = 1;
	
	private static final String[] alphabets = new String[] {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
	
	private static String S_Table="";
	private static String N_Table="";
		
	private static final String CREATE_VOTEADD	= "CREATE TABLE VoteAdd (WARD_NO text,PART_NO text, VOTE_ADD text, PERMISSION text);";
	
	private static final String CREATE_BACKUP  = "CREATE TABLE Backup (sr_no text,mob_no text,email_id text);";

	//private static final String CREATE_FullVidhansabha = "CREATE TABLE FullVidhansabha ( WARD_NO text,PART_NO text,SR_NO text,SEX text,AGE text,BUILD_NAME text,AREA_NAME text,HOUSE_NO text,FIRST_NAME text,LASTNAME text,MFIRST_NAME text,MLASTNAME text, MADDRESS text);";
	//csv sequence
	//ward_no,booth_no,serial_no,sex,age,address_en,house_no_en,
	// first_name_en,last_name_en,address_marathi,house_no_marathi,
	// first_name_marathi,last_name_marathii,ac_no,
	// original_part_no,original_serial_no,card_no,section_no,
	// mobile_no,lang

	private static final String CREATE_FullVidhansabha = "CREATE TABLE FullVidhansabha" +
			" ( WARD_NO text,PART_NO text,SR_NO text,SEX text,AGE" +
			" text,BUILD_NAME text,AREA_NAME text,HOUSE_NO text," +
			"FIRST_NAME text,LASTNAME text," +
			"ADDRESS_MARATHI text, HOUSE_NO_MARATHI text, " +
			"FIRSTNAME_MARATHI text, LASTNAME_MARATHI text," +
			"ACC_NO text,ORG_PART_NO text,ORG_SERIAL_NO text," +
			"CARD_NO text,SECTION_NO text,MOBILE_NO text,LANG text );";

	private static final String CREATE_UPDATE_VOTING_ADDRESS  = "CREATE TABLE UpdateVotingAddress(Id INTEGER PRIMARY KEY AUTOINCREMENT,PART_NO text,WARD_NO text,VOTE_ADD text,SR_FROM text,SR_UPTO text,VOTE_ADD_MARATHI text);";

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db)
	{
		/*for(int i = 0; i < alphabets.length; i++)
		{
			S_Table = "CREATE TABLE " + "Stable"+ alphabets[i] + " (WARD_NO text, PART_NO text,SR_NO text,SEX text,AGE text,BUILD_NAME text,AREA_NAME text,HOUSE_NO text,FIRST_NAME text,LASTNAME text);";
			db.execSQL(S_Table);
			N_Table = "CREATE TABLE " + "Ntable"+ alphabets[i] + " ( WARD_NO text,PART_NO text,SR_NO text,SEX text,AGE text,BUILD_NAME text,AREA_NAME text,HOUSE_NO text,FIRST_NAME text,LASTNAME text);";
			db.execSQL(N_Table);
		}*/
		
		db.execSQL(CREATE_FullVidhansabha);
		db.execSQL(CREATE_VOTEADD);
		db.execSQL(CREATE_BACKUP);
		db.execSQL(CREATE_UPDATE_VOTING_ADDRESS);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		
		for(int i = 0; i < alphabets.length; i++)
		{
			S_Table = "DROP TABLE IF EXISTS " + "Stable"+ alphabets[i];
			db.execSQL(S_Table);
			N_Table = "DROP TABLE IF EXISTS " + "Ntable"+ alphabets[i];
			db.execSQL(N_Table);
		}
		
		db.execSQL("DROP TABLE IF EXISTS FullVidhansabha");
		db.execSQL("DROP TABLE IF EXISTS VoteAdd");
		db.execSQL("DROP TABLE IF EXISTS Backup");
		onCreate(db);

	}
}
