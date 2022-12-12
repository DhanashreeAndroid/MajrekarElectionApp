package voterSearch.app.SmdInfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.util.ArrayList;

public class DBAdapter {

    public static final String key_WARD_NO = "WARD_NO";
    public static final String key_SR_NO = "SR_NO";
    public static final String key_SEX = "SEX";
    public static final String key_AGE = "AGE";
    public static final String key_BUILD_NAME = "BUILD_NAME";
    public static final String key_AREA_NAME = "AREA_NAME";
    public static final String key_HOUSE_NO = "HOUSE_NO";
    public static final String key_FIRST_NAME = "FIRST_NAME";
    public static final String key_LASTNAME = "LASTNAME";
    public static final String key_MFIRST_NAME = "FIRSTNAME_MARATHI";
    public static final String key_MLASTNAME = "LASTNAME_MARATHI";
    public static final String key_MADDRESS = "ADDRESS_MARATHI";
    public static final String key_MHOUSE_NO = "HOUSE_NO_MARATHI";
    public static final String key_ACC_NO = "ACC_NO";
    public static final String key_ORG_PART_NO = "ORG_PART_NO";
    public static final String key_ORG_SERIAL_NO = "ORG_SERIAL_NO";
    public static final String key_CARD_NO = "CARD_NO";
    public static final String key_SECTION_NO = "SECTION_NO";
    public static final String key_MOBILE_NO = "MOBILE_NO";
    public static final String key_LANG = "LANG";


    private static final String TABLE_VOTEADD = "VoteAdd";
    public static final String key_PART_NO = "PART_NO";
    public static final String key_VOTE_ADD = "VOTE_ADD";
    public static final String key_PERMISSION = "PERMISSION";

    private static final String TABLE_BACKUP = "Backup";
    public static final String Counter = "Counter";
    public static final String part_no = "part_no";
    public static final String sr_no = "sr_no";
    public static final String mob_no = "mob_no";
    public static final String email_id = "email_id";

    private static final String TABLE_UPADTE_VOTING_ADDRESS = "UpdateVotingAddress";
    public static final String key_UPDATE_VOTING_ADDRESS_PRIMAY_ID = "Id";
    public static final String key_sr_From = "SR_FROM";
    public static final String key_sr_To = "SR_UPTO";
    public static final String key_VOTING_ADDRESS_MARATHI = "VOTE_ADD_MARATHI";

    private static final String[] alphabets = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    SQLiteDatabase mDb;
    Context mCtx;
    DBHelper mDbHelper;

    public DBAdapter(Context context) {
        this.mCtx = context;
    }

    public DBAdapter open() throws SQLException {
        mDbHelper = new DBHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    //----------------Inserting values---------------------------------------------------------------------

    public long createTableFullVidhansabha(String table, String j, String a, String b, String c, String d, String e, String f, String g, String h, String i,
                                           String mfirstname, String mlastname, String madd) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(key_WARD_NO, j);
        initialValues.put(key_PART_NO, a);
        initialValues.put(key_SR_NO, b);
        initialValues.put(key_SEX, c);
        initialValues.put(key_AGE, d);
        initialValues.put(key_BUILD_NAME, e);
        initialValues.put(key_AREA_NAME, f);
        initialValues.put(key_HOUSE_NO, g);
        initialValues.put(key_FIRST_NAME, h);
        initialValues.put(key_LASTNAME, i);
        initialValues.put(key_MFIRST_NAME, mfirstname);
        initialValues.put(key_MLASTNAME, mlastname);
        initialValues.put(key_MADDRESS, madd);

        return mDb.insert(table, null, initialValues);
    }

    public long createTable(String table, String j, String a, String b, String c, String d, String e, String f, String g, String h, String i,
                            String MFIRST_NAME, String MLASTNAME, String MADDRESS,
                            String MHOUSE_NO, String ACC_NO, String orgPartNo,
                            String ORG_SERIAL_NO, String CARD_NO, String SECTION_NO,
                            String MOBILE_NO, String LANG
    ) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(key_WARD_NO, j);
        initialValues.put(key_PART_NO, a);
        initialValues.put(key_SR_NO, b);
        initialValues.put(key_SEX, c);
        initialValues.put(key_AGE, d);
        initialValues.put(key_BUILD_NAME, e);
        initialValues.put(key_AREA_NAME, f);
        initialValues.put(key_HOUSE_NO, g);
        initialValues.put(key_FIRST_NAME, h);
        initialValues.put(key_LASTNAME, i);
        initialValues.put(key_MLASTNAME, MLASTNAME);
        initialValues.put(key_MFIRST_NAME, MFIRST_NAME);
        initialValues.put(key_MADDRESS, MADDRESS);
        initialValues.put(key_MHOUSE_NO, MHOUSE_NO);
        initialValues.put(key_ACC_NO, ACC_NO);
        initialValues.put(key_ORG_PART_NO, orgPartNo);
        initialValues.put(key_ORG_SERIAL_NO, ORG_SERIAL_NO);
        initialValues.put(key_CARD_NO, CARD_NO);
        initialValues.put(key_SECTION_NO, SECTION_NO);
        initialValues.put(key_MOBILE_NO, MOBILE_NO);
        initialValues.put(key_LANG, LANG);

        return mDb.insert(table, null, initialValues);
    }

    //-------------------------------------------------------------------------------------------------------
    public long createVoteAdd(String d, String a, String b, String c) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(key_WARD_NO, d);
        initialValues.put(key_PART_NO, a);
        initialValues.put(key_VOTE_ADD, b);
        initialValues.put(key_PERMISSION, c);

        return mDb.insert(TABLE_VOTEADD, null, initialValues);
    }

    //-------------------------------------------------------------------------------------------------------


    public long createBackup(String sr, String mob, String email) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(sr_no, sr);
        initialValues.put(mob_no, mob);
        initialValues.put(email_id, email);

        return mDb.insert(TABLE_BACKUP, null, initialValues);
    }

    public void UpdateBackup(String Serial, String col_name, String Col_value) {
        String strUpdate = "Update Backup set " + col_name + " = '" + Col_value + "' where sr_no = '" + Serial + "'";

        Cursor cur = mDb.rawQuery(strUpdate, null);

        if (cur != null) {
            cur.moveToFirst();
        }
    }

    public boolean isRecordExists(String tableName, String referenceColoumnName, String strWhereClause) {
        Cursor cursor = null;
        String query = "SELECT COUNT(" + referenceColoumnName + ") FROM "
                + tableName
                + " WHERE "
                + strWhereClause + ";";
        try {
            cursor = mDb.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    try {
                        int count = Integer.parseInt(cursor.getString(0));
                        if (count > 0) {
                            return true;
                        }
                    } catch (Exception e) {
                        return false;
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            return false;
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }

        }
        return false;
    }


    //-------------------------------------------------------------------------------------------------------
    public long insert_UpdateVotingAddress(String wardNo, String votingCenter, String votingAdd, String srFrom, String srTo, String add_marathi) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(key_WARD_NO, wardNo);
        initialValues.put(key_PART_NO, votingCenter);
        initialValues.put(key_VOTE_ADD, votingAdd);
        initialValues.put(key_sr_From, srFrom);
        initialValues.put(key_sr_To, srTo);
        initialValues.put(key_VOTING_ADDRESS_MARATHI, add_marathi);
        return mDb.insert(TABLE_UPADTE_VOTING_ADDRESS, null, initialValues);
    }

    //-------------------------------------------------------------------------------------------------------
    public long save_UpdateVotingAddress(FillVotingCenterVo vo) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(key_WARD_NO, vo.getWardNo());
        initialValues.put(key_PART_NO, vo.getVotingCenter());
        initialValues.put(key_VOTE_ADD, vo.getAddress());
        initialValues.put(key_sr_From, vo.getSrFrom());
        initialValues.put(key_sr_To, vo.getSrUpTo());
        if (!TextUtils.isEmpty(vo.getPrimaryId())) {
            return mDb.updateWithOnConflict(TABLE_UPADTE_VOTING_ADDRESS, initialValues,
                    key_UPDATE_VOTING_ADDRESS_PRIMAY_ID + "= " + vo.getPrimaryId(),
                    null, SQLiteDatabase.CONFLICT_IGNORE);
        } else {
            return mDb.insert(TABLE_UPADTE_VOTING_ADDRESS, null, initialValues);
        }
    }

    public void updateBoothNo(String boothNo, String srFrom, String srTo) {
        /*ContentValues initialValues = new ContentValues();
        initialValues.put(key_PART_NO, boothNo);
            return mDb.updateWithOnConflict("FullVidhansabha", initialValues,
                    key_SR_NO + " between " + srFrom +" and "+ srTo,
                    null,  SQLiteDatabase.CONFLICT_IGNORE);*/
        String strUpdate = "Update FullVidhansabha set " + key_PART_NO + " = " + boothNo + " where " + key_SR_NO + " >=   cast("+ srFrom + "  as integer)   and " + key_SR_NO + " <=  cast("+ srTo + "  as integer) ";

        Cursor cur = mDb.rawQuery(strUpdate, null);

        if (cur != null) {
            cur.moveToFirst();
        }

    }

    //------------------------------------------------------------------------------------------

    public String checkSerialNoAvailability(String SrFrom, String SrTo) {
        String str = null;
        Cursor c = mDb.rawQuery("select " + key_sr_From + "," + key_sr_To + " from " + TABLE_UPADTE_VOTING_ADDRESS, null);
        if (c != null && c.moveToFirst()) {
            do {
                if (SrFrom.equalsIgnoreCase(c.getString(c.getColumnIndex(key_sr_From)))) {
                    str = "Serial From : " + c.getString(c.getColumnIndex(key_sr_From));
                }
                if (SrTo.equalsIgnoreCase(c.getString(c.getColumnIndex(key_sr_To)))) {
                    if (TextUtils.isEmpty(str)) {
                        str = "Serial To : " + c.getString(c.getColumnIndex(key_sr_To));
                    } else {
                        str += ",Serial To : " + c.getString(c.getColumnIndex(key_sr_To));
                    }

                }
            } while (c.moveToNext());
        }
        if (!TextUtils.isEmpty(str)) {
            str += " already exists, Please enter correct. ";
        }

        return str;
    }

    //-------------------------------------------------------------------------------------------------------
    public void updateAllVotingCenter_WardNo(String votingCenter, String wardNo, String SerialFrom, String SerialTo) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(key_WARD_NO, wardNo);
        initialValues.put(key_PART_NO, votingCenter);

        for (int i = 0; i < alphabets.length; i++) {
            updateTables("Stable" + alphabets[i], SerialFrom, SerialTo, initialValues);
            updateTables("Ntable" + alphabets[i], SerialFrom, SerialTo, initialValues);
        }
        updateTables("FullVidhansabha", SerialFrom, SerialTo, initialValues);
    }

    private void updateTables(String table, String SerialFrom, String SerialTo, ContentValues initialValues) {
        String query = "select " + key_SR_NO +
                " from  " + table + " where SR_NO between  '" + SerialFrom + "' and '" + SerialTo + "' ";
        Cursor c = mDb.rawQuery(query, null);
        if (c != null && c.moveToFirst()) {
            do {
                if (isRecordExists(table, key_SR_NO, key_SR_NO + "= '" + c.getString(c.getColumnIndex(key_SR_NO)) + "' ")) {
                    mDb.updateWithOnConflict(table, initialValues, key_SR_NO + "= '" + c.getString(c.getColumnIndex(key_SR_NO)) + "' ",
                            null, SQLiteDatabase.CONFLICT_IGNORE);
                }

            } while (c.moveToNext());
        }
    }


    //public Cursor query (boolean distinct, String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit)
    public void delete_UpdateVotingAddress() {
        String query = "delete  from  " + TABLE_UPADTE_VOTING_ADDRESS;
        Cursor c = mDb.rawQuery(query, null);
        if (c != null) {
            c.moveToFirst();
        }
    }

    public ArrayList<FillVotingCenterVo> getFillVotingCenter() {
        ArrayList<FillVotingCenterVo> list = new ArrayList<FillVotingCenterVo>();
        // key_LASTNAME + " ASC, " + key_FIRST_NAME  + " ASC"---------order by(on last null)
        Cursor c = mDb.rawQuery("select * from " + TABLE_UPADTE_VOTING_ADDRESS, null);
        if (c != null && c.moveToFirst()) {

            do {
                FillVotingCenterVo vo = new FillVotingCenterVo();
                vo.setPrimaryId(c.getString(c.getColumnIndex(key_UPDATE_VOTING_ADDRESS_PRIMAY_ID)));
                vo.setVotingCenter(c.getString(c.getColumnIndex(key_PART_NO)));
                vo.setAddress(c.getString(c.getColumnIndex(key_VOTE_ADD)));
                vo.setSrFrom(c.getString(c.getColumnIndex(key_sr_From)));
                vo.setSrUpTo(c.getString(c.getColumnIndex(key_sr_To)));
                vo.setWardNo(c.getString(c.getColumnIndex(key_WARD_NO)));
                list.add(vo);
            } while (c.moveToNext());
        }
        return list;
    }

    public void delete_VotingAddress_centerWise(String primaryId) {
        String query = "delete  from  " + TABLE_UPADTE_VOTING_ADDRESS + " where " + key_UPDATE_VOTING_ADDRESS_PRIMAY_ID + "= '" + primaryId + "'";
        Cursor c = mDb.rawQuery(query, null);
        if (c != null) {
            c.moveToFirst();
        }
    }
    //----------------Retrieving Voter name---------------------------------------------------------------------

    /*//public Cursor query (boolean distinct, String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit)
    public Cursor fetchVoterName(String table) {
        // key_LASTNAME + " ASC, " + key_FIRST_NAME  + " ASC"---------order by(on last null)
        Cursor c = mDb.query("FullVidhansabha", new String[]{key_LASTNAME, key_FIRST_NAME, key_SR_NO, key_PART_NO, key_WARD_NO,
                key_MLASTNAME, key_MFIRST_NAME}, key_LASTNAME + "  like '" + table + "%'", null, null, null, key_LASTNAME + " ASC, " + key_FIRST_NAME  + " ASC");

        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }*/

    //public Cursor query (boolean distinct, String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit)
    public Cursor fetchVoterName(String table, String type ) {
        // key_LASTNAME + " ASC, " + key_FIRST_NAME  + " ASC"---------order by(on last null)
        Cursor c;
        if (type.equals("surname")) {
            c = mDb.query(table, new String[]{key_LASTNAME, key_FIRST_NAME, key_SR_NO, key_PART_NO, key_WARD_NO, key_MFIRST_NAME, key_MLASTNAME
            }, null, null, null, null, key_LASTNAME + " COLLATE NOCASE ASC, " + key_FIRST_NAME + " COLLATE NOCASE ASC");

        }else {
            c = mDb.query(table, new String[]{key_LASTNAME, key_FIRST_NAME, key_SR_NO, key_PART_NO, key_WARD_NO, key_MFIRST_NAME, key_MLASTNAME
            }, null, null, null, null, key_FIRST_NAME + " COLLATE NOCASE ASC, " + key_LASTNAME + " COLLATE NOCASE ASC");

        }


        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public Cursor fetchVoterNameForLanguage(String table, String surname,  String language,String buildName ) {
        // key_LASTNAME + " ASC, " + key_FIRST_NAME  + " ASC"---------order by(on last null)
        Cursor c;
        c = mDb.query(table, new String[]{key_LASTNAME, key_FIRST_NAME, key_SR_NO, key_PART_NO, key_WARD_NO, key_MFIRST_NAME, key_MLASTNAME
        }, key_LASTNAME + " = '"+surname+"' And " + key_BUILD_NAME
                + "= '"+buildName+"' And " + key_LANG
                + "='"+language+"'", null, null, null, key_LASTNAME + " COLLATE NOCASE ASC, " + key_FIRST_NAME + " COLLATE NOCASE ASC");



        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }



    public Cursor getSurnameAndCountForLang(String buildName, String lang) {
        String query = "select   LASTNAME , " +
                "count(LASTNAME ) as cont " +
                "from FullVidhansabha where BUILD_NAME = '"+buildName+"' and LANG = '"+lang+"' group by LASTNAME";
        Cursor c = mDb.rawQuery(query, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }


    public Cursor fetchVoterNameForBindingToACT(String table, String wardNo) {
        String query = "select Distinct " + key_LASTNAME + " from " + table + " where WARD_NO = " + wardNo;
        Cursor c = mDb.rawQuery(query, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public Cursor fetchVoterSurNameForCount(String table,String wadrNO, String LastName) {
        // key_LASTNAME + " ASC, " + key_FIRST_NAME  + " ASC"---------order by(on last null)
        Cursor c = mDb.query(table, new String[]{key_LASTNAME, key_FIRST_NAME},
                key_LASTNAME + " = '" + LastName + "' and WARD_NO = " + wadrNO, null, null, null, null);

        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }


    //----------------Retrieving Voter Details---------------------------------------------------------------------

    public Cursor fetchVoterDetailsByName(String table, String LastName, String FirstName, String sr_no, String part, String ward) {
        //PART_NO ,SR_NO ,SEX ,AGE ,BUILD_NAME ,AREA_NAME ,HOUSE_NO,FIRST_NAME ,LASTNAME
       /* String query = "SELECT " +
                table + ".WARD_NO," +
                table + ".PART_NO," + table + ".SR_NO," +
                table + ".SEX, " + table + ".AGE, " + table + ".BUILD_NAME," +
                table + ".AREA_NAME," + table + ".HOUSE_NO, UpdateVotingAddress.VOTE_ADD " +
                " FROM " + table + " LEFT JOIN " + TABLE_UPADTE_VOTING_ADDRESS +
                " ON " + table + ".PART_NO = UpdateVotingAddress.PART_NO " +
                " And " + table + ".WARD_NO = UpdateVotingAddress.WARD_NO " +
                " where " + table + ".LASTNAME = '" + LastName + "' And " + table + ".FIRST_NAME = '" + FirstName + "'" +
                " And " + table + ".SR_NO = '" + sr_no + "' " +
                " And " + table + ".PART_NO = '" + part + "' " +
                " And " + table + ".WARD_NO = '" + ward + "' ";
       */
        String query;
        Cursor cur = mDb.rawQuery("select PART_NO from " + TABLE_UPADTE_VOTING_ADDRESS, null);
        if (cur != null && cur.getCount() == 0) {
            query = "SELECT " +
                    table + ".WARD_NO," +
                    table + ".PART_NO," + table + ".SR_NO," +
                    table + ".SEX, " + table + ".AGE, " + table + ".BUILD_NAME," +
                    table + ".AREA_NAME," + table + ".HOUSE_NO, '' as VOTE_ADD ," +
                    table +".FIRSTNAME_MARATHI," +table +".LASTNAME_MARATHI," +table +".ADDRESS_MARATHI," +
                    table +".HOUSE_NO_MARATHI" +
                    " FROM " + table +
                    " where " +
                    table + ".SR_NO = '" + sr_no + "' and " +
                    table + ".PART_NO = '" + part + "' ";

        } else {
            query = "SELECT " +
                    TABLE_UPADTE_VOTING_ADDRESS + ".WARD_NO," +
                    TABLE_UPADTE_VOTING_ADDRESS + ".PART_NO," + table + ".SR_NO," +
                    table + ".SEX, " + table + ".AGE, " + table + ".BUILD_NAME," +
                    table + ".AREA_NAME," + table + ".HOUSE_NO, UpdateVotingAddress.VOTE_ADD ," +
                    table +".FIRSTNAME_MARATHI," +table +".LASTNAME_MARATHI," +table +".ADDRESS_MARATHI," +
                    table +".HOUSE_NO_MARATHI, UpdateVotingAddress.VOTE_ADD_MARATHI" +
                    " FROM " + table
                    + ", " + TABLE_UPADTE_VOTING_ADDRESS +
                    " where " +
                     table + ".SR_NO between " +
                    " cast(UpdateVotingAddress.SR_FROM as integer) and cast(UpdateVotingAddress.SR_UPTO as integer) " +
                     " And " +
                    table + ".SR_NO = '" + sr_no + "'  And  " +
                    table + ".PART_NO = '" + part + "' " +
                    " And  " + table + ".PART_NO = UpdateVotingAddress.PART_NO" +
                    " And  " + table + ".WARD_NO = UpdateVotingAddress.WARD_NO";

        }
        cur.close();
/*
        String wardQuery = "(select UpdateVotingAddress.WARD_NO from UpdateVotingAddress where " + table + ".SR_NO between "+
                " UpdateVotingAddress.SR_FROM and UpdateVotingAddress.SR_UPTO ) as WARD_NO," ;
        String partQuery = "(select UpdateVotingAddress.PART_NO from UpdateVotingAddress where " + table + ".SR_NO between "+
                 " UpdateVotingAddress.SR_FROM and UpdateVotingAddress.SR_UPTO ) as PART_NO," ;
        String addQuery = "(select UpdateVotingAddress.VOTE_ADD from UpdateVotingAddress where " + table + ".SR_NO between "+
                " UpdateVotingAddress.SR_FROM and UpdateVotingAddress.SR_UPTO ) as VOTE_ADD " ;

        String query = "SELECT " +
                wardQuery + partQuery +
                table + ".SR_NO," +
                table + ".SEX, " + table + ".AGE, " + table + ".BUILD_NAME," +
                table + ".AREA_NAME," + table + ".HOUSE_NO, "+
                addQuery + " FROM " + table +
                " where " + table + ".LASTNAME = '" + LastName + "' And " + table + ".FIRST_NAME = '" + FirstName + "'" +
                " And " + table + ".SR_NO = '" + sr_no + "' " ;
*/
        Cursor c = mDb.rawQuery(query, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public Cursor CheckForVoterDeleted(String table, String LastName, String FirstName, String sr) {
        Cursor c = mDb.query(table, new String[]{key_PART_NO, key_SR_NO, key_SEX},
                key_LASTNAME + " = '" + LastName + "' And " +
                        key_FIRST_NAME + " = '" + FirstName + "' And " +
                        key_SR_NO + " = '" + sr + "' And " +
                        key_SEX + " = 'D' or " + key_SEX + " = 'd'",
                null,
                null,
                null,
                null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }


    //----------------Retrieving Voter family person names---------------------------------------------------------------------

    public Cursor fetchVoterFamilyPersonName(String table, String house_no, String build_name, String area_name, String last_name) {
        Cursor c = mDb.query(table, new String[]{key_LASTNAME, key_FIRST_NAME, key_SR_NO, key_SEX, key_PART_NO, key_WARD_NO, key_MFIRST_NAME, key_MLASTNAME},
                key_HOUSE_NO + " = '" + house_no + "' And " +
                        key_BUILD_NAME + " = '" + build_name + "' And " +
                        key_AREA_NAME + " = '" + area_name + "' And " +
                        key_LASTNAME + " = '" + last_name + "'",
                null,
                null,
                null,
                null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public Cursor fetchVoterFamilyPersonDetails(String table, String house_no, String build_name, String area_name, String last_name) {
        //PART_NO ,SR_NO ,SEX ,AGE ,BUILD_NAME ,AREA_NAME ,HOUSE_NO,FIRST_NAME ,LASTNAME

        String query;
        Cursor cursor = mDb.rawQuery("select PART_NO from " + TABLE_UPADTE_VOTING_ADDRESS, null);
        if (cursor != null && cursor.getCount() == 0) {

            query = "SELECT " +
                    table + ".WARD_NO, " +
                    table + ".LASTNAME, " + table + ".FIRST_NAME, " +
                    table + ".PART_NO, " + table + ".SR_NO, " +
                    table + ".SEX, " + table + ".AGE, " + table + ".BUILD_NAME," +
                    table + ".AREA_NAME," + table + ".HOUSE_NO, '' as VOTE_ADD, " +
                    table +".FIRSTNAME_MARATHI," +table +".LASTNAME_MARATHI," +table +".ADDRESS_MARATHI," +
                    table +".HOUSE_NO_MARATHI" +
                    " FROM " + table +
                    " where " + table + ".HOUSE_NO = '" + house_no + "' And " + table + ".BUILD_NAME = '" + build_name + "'" +
                    " And " + table + ".AREA_NAME = '" + area_name + "' And " + table + ".LASTNAME = '" + last_name + "'";

        } else {

            query = "SELECT " +
                    TABLE_UPADTE_VOTING_ADDRESS + ".WARD_NO, " +
                    table + ".LASTNAME, " + table + ".FIRST_NAME, " +
                    TABLE_UPADTE_VOTING_ADDRESS + ".PART_NO, " + table + ".SR_NO, " +
                    table + ".SEX, " + table + ".AGE, " + table + ".BUILD_NAME," +
                    table + ".AREA_NAME," + table + ".HOUSE_NO, UpdateVotingAddress.VOTE_ADD, " +
                    table +".FIRSTNAME_MARATHI," +table +".LASTNAME_MARATHI," +table +".ADDRESS_MARATHI," +
                    table +".HOUSE_NO_MARATHI, UpdateVotingAddress.VOTE_ADD_MARATHI" +
                    " FROM " + table
                    + ", " + TABLE_UPADTE_VOTING_ADDRESS +
                    " where " +
                    table + ".SR_NO between " +
                     " cast(UpdateVotingAddress.SR_FROM as integer) and cast(UpdateVotingAddress.SR_UPTO as integer) " +
                    " And " +
                    table + ".HOUSE_NO = '" + house_no + "' And " + table + ".BUILD_NAME = '" + build_name + "'" +
                    " And " + table + ".AREA_NAME = '" + area_name + "' And " + table + ".LASTNAME = '" + last_name + "'" +
                    " And " + table + ".PART_NO = UpdateVotingAddress.PART_NO" +
                    " And  " + table + ".WARD_NO = UpdateVotingAddress.WARD_NO";

        }
        cursor.close();


        Cursor c = mDb.rawQuery(query, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    //Getting all records from table VoteAdd-----------------------------

    public Cursor GetAllVoterAdd() {
        return mDb.query(TABLE_VOTEADD, new String[]{key_WARD_NO, key_PART_NO,
                key_VOTE_ADD}, null, null, null, null, null);
    }

    public Cursor getAddressByPartWardWise(String part, String ward) {
        Cursor c = mDb.query(TABLE_UPADTE_VOTING_ADDRESS, new String[]{key_WARD_NO, key_PART_NO, key_VOTE_ADD},
                key_WARD_NO + " = '" + ward + "' And " +
                        key_PART_NO + " = '" + part + "'",
                null,
                null,
                null,
                null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }


    //Selecting record from Backup table------------------------------------------

    public Cursor FetchBackup(String table, String Serial) {

        String str = "select Backup.sr_no,Backup.mob_no ,Backup.email_id from Backup Left Outer join " + table + " on " + table + ".SR_NO = Backup.sr_no where " + table + ".sr_no = '" + Serial + "'";

        Cursor cur = mDb.rawQuery(str, null);

        if (cur != null) {
            cur.moveToFirst();
        }

        return cur;
    }


    //-----------Report Related------------------------------------------------------------

/*
    public Cursor Get_serial_In_Part(String part) {
        StringBuilder str = new StringBuilder();

        for (int i = 0; i < alphabets.length; i++) {
            if (alphabets[i].equals("Z")) {
                str.append("Select SR_NO from Stable" + alphabets[i] + " where PART_NO = '" + part + "'");
            } else {
                str.append("Select SR_NO from Stable" + alphabets[i] + " where PART_NO = '" + part + "' Union All ");
            }
        }

        Cursor cur = mDb.rawQuery(str.toString(), null);

        if (cur != null) {
            cur.moveToFirst();
        }

        return cur;
    }
*/

    public Cursor Get_serial_In_Part(String part, String ward) {
        String str = "Select SR_NO, WARD_NO from FullVidhansabha where FullVidhansabha.PART_NO = '" + part + "' and FullVidhansabha.WARD_NO = '" + ward + "'";
        // Cursor cursor = mDb.rawQuery("select PART_NO from " + TABLE_UPADTE_VOTING_ADDRESS, null);
        /*if (cursor != null && cursor.getCount() != 0) {
             str = "Select SR_NO , WARD_NO from FullVidhansabha,UpdateVotingAddress where UpdateVotingAddress.PART_NO = '" + part + "' and FullVidhansabha.WARD_NO = '"+ward+"'";
        }*/
        //cursor.close();
        Cursor cur = mDb.rawQuery(str, null);

        if (cur != null) {
            cur.moveToFirst();
        }

        return cur;

    }

    public Cursor fetchVoterDetailsByPartSr(String sr, String part_no, String vidhansabhaNo) {
        //PART_NO ,SR_NO ,SEX ,AGE ,BUILD_NAME ,AREA_NAME ,HOUSE_NO,FIRST_NAME ,LASTNAME

        StringBuilder str = new StringBuilder();

        Cursor cur = mDb.rawQuery("select PART_NO from " + TABLE_UPADTE_VOTING_ADDRESS, null);
        if (cur != null && cur.getCount() != 0) {
            for (int i = 0; i < alphabets.length; i++) {
                String table = "Stable" + alphabets[i];

                if (alphabets[i].equals("Z")) {
                    str.append("SELECT " +
                            TABLE_UPADTE_VOTING_ADDRESS + ".WARD_NO," +
                            TABLE_UPADTE_VOTING_ADDRESS + ".PART_NO," + table + ".SR_NO," +
                            table + ".LASTNAME," + table + ".FIRST_NAME," +
                            table + ".SEX, " + table + ".AGE, " + table + ".BUILD_NAME," +
                            table + ".AREA_NAME," + table + ".HOUSE_NO, UpdateVotingAddress.VOTE_ADD, " +
                            "'" + table + "' as TableName " +
                            " FROM " + table
                            + ", " + TABLE_UPADTE_VOTING_ADDRESS +
                            " where " +
                            //table + ".SR_NO between " +
                            // "  cast(UpdateVotingAddress.SR_FROM as integer)  and  cast(UpdateVotingAddress.SR_UPTO  as integer) " +
                            // " And " +
                            TABLE_UPADTE_VOTING_ADDRESS + ".PART_NO  =  " + table + ".PART_NO  and " +
                            TABLE_UPADTE_VOTING_ADDRESS + ".WARD_NO  =  " + table + ".WARD_NO  and " +
                            table + ".SR_NO = '" + sr + "' and " +
                            table + ".PART_NO = '" + part_no + "' and " +
                            table + ".WARD_NO = '" + vidhansabhaNo + "' ");

                } else {
                    str.append("SELECT " +
                            TABLE_UPADTE_VOTING_ADDRESS + ".WARD_NO," +
                            TABLE_UPADTE_VOTING_ADDRESS + ".PART_NO," + table + ".SR_NO," +
                            table + ".LASTNAME," + table + ".FIRST_NAME," +
                            table + ".SEX, " + table + ".AGE, " + table + ".BUILD_NAME," +
                            table + ".AREA_NAME," + table + ".HOUSE_NO, UpdateVotingAddress.VOTE_ADD, " +
                            "'" + table + "' as TableName " +
                            " FROM " + table
                            + ", " + TABLE_UPADTE_VOTING_ADDRESS +
                            " where " +
                            //table + ".SR_NO between " +
                            //"  cast(UpdateVotingAddress.SR_FROM as integer)  and  cast(UpdateVotingAddress.SR_UPTO  as integer) " +
                            //" And " +
                            TABLE_UPADTE_VOTING_ADDRESS + ".PART_NO  =  " + table + ".PART_NO  and " +
                            TABLE_UPADTE_VOTING_ADDRESS + ".WARD_NO  =  " + table + ".WARD_NO  and " +
                            table + ".SR_NO = '" + sr + "' and " +
                            table + ".PART_NO = '" + part_no + "' and " +
                            table + ".WARD_NO = '" + vidhansabhaNo + "' "
                            + " Union ");
                }
            }
        } else {
            for (int i = 0; i < alphabets.length; i++) {
                String table = "Stable" + alphabets[i];

                if (alphabets[i].equals("Z")) {
                    str.append("SELECT " +
                            table + ".WARD_NO," +
                            table + ".PART_NO," + table + ".SR_NO," +
                            table + ".LASTNAME," + table + ".FIRST_NAME," +
                            table + ".SEX, " + table + ".AGE, " + table + ".BUILD_NAME," +
                            table + ".AREA_NAME," + table + ".HOUSE_NO, '' as VOTE_ADD, " +
                            "'" + table + "' as TableName " +
                            " FROM " + table +
                            " where " +
                            table + ".SR_NO = '" + sr + "' and " +
                            table + ".PART_NO = '" + part_no + "' and " +
                            table + ".WARD_NO = '" + vidhansabhaNo + "'");


                } else {
                    str.append("SELECT " +
                            table + ".WARD_NO," +
                            table + ".PART_NO," + table + ".SR_NO," +
                            table + ".LASTNAME," + table + ".FIRST_NAME," +
                            table + ".SEX, " + table + ".AGE, " + table + ".BUILD_NAME," +
                            table + ".AREA_NAME," + table + ".HOUSE_NO, '' as VOTE_ADD, " +
                            "'" + table + "' as TableName " +
                            " FROM " + table +
                            " where " +
                            table + ".SR_NO = '" + sr + "' and " +
                            table + ".PART_NO = '" + part_no + "' and " +
                            table + ".WARD_NO = '" + vidhansabhaNo + "' "
                            + " Union ");
                }
            }

        }
        cur.close();
        Cursor c = mDb.rawQuery(str.toString(), null);

        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }


    public Cursor fetchVoterDetailsByPartNo(String part) {
        //PART_NO ,SR_NO ,SEX ,AGE ,BUILD_NAME ,AREA_NAME ,HOUSE_NO,FIRST_NAME ,LASTNAME


        StringBuilder str = new StringBuilder();

        for (int i = 0; i < alphabets.length; i++) {
            String table = "Stable" + alphabets[i];

            if (alphabets[i].equals("Z")) {
                str.append("SELECT Cast(SR_NO as integer)," +
                        table + ".LASTNAME," + table + ".FIRST_NAME," +
                        table + ".SEX, " + table + ".AGE, " + table + ".BUILD_NAME," +
                        table + ".AREA_NAME," + table + ".HOUSE_NO, UpdateVotingAddress.VOTE_ADD, " +
                        "'" + table + "' as TableName " +
                        " FROM " + table + " LEFT JOIN " + TABLE_UPADTE_VOTING_ADDRESS +
                        " ON " + table + ".PART_NO = UpdateVotingAddress.PART_NO " +
                        " where " + table + ".PART_NO = '" + part + "' order by Cast(SR_NO as integer) ASC ");
            } else {
                str.append("SELECT Cast(SR_NO as integer)," +
                        table + ".LASTNAME," + table + ".FIRST_NAME," +
                        table + ".SEX, " + table + ".AGE, " + table + ".BUILD_NAME," +
                        table + ".AREA_NAME," + table + ".HOUSE_NO, UpdateVotingAddress.VOTE_ADD, " +
                        "'" + table + "' as TableName " +
                        " FROM " + table + " LEFT JOIN " + TABLE_UPADTE_VOTING_ADDRESS +
                        " ON " + table + ".PART_NO = UpdateVotingAddress.PART_NO " +
                        " where " + table + ".PART_NO = '" + part + "' Union ");
            }
        }

        Cursor c = mDb.rawQuery(str.toString(), null);

        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    //String str = "select max(CAST(PART_NO as INTEGER)) as part from VoteAdd";
    ///Cursor cur = mDbAdapter.mDb.rawQuery(str, null);
    //int Total_part_no = cur.getInt(0);
    //cur.close();


    //----------Building wise Search In Part-----------------------------------------------------
    public Cursor GetBuildingNames() {
        String query = "select distinct BUILD_NAME from FullVidhansabha order by  BUILD_NAME";
        Cursor c = mDb.rawQuery(query, null);
        return c;
    }

    public Cursor GetLanguages(String buildName) {
        String query = "select distinct LANG from FullVidhansabha where BUILD_NAME = '"+buildName+"'";
        Cursor c = mDb.rawQuery(query, null);
        return c;
    }

    public Cursor GetLanguageFromBuilding(String buildingName) {
        String query = "select distinct LANG from FullVidhansabha WHERE BUILD_NAME = '" + buildingName + "'";
        Cursor c = mDb.rawQuery(query, null);
        return c;
    }
    public Cursor GetAddress_PartNoWise(String partNo, String wardNo) {
        String query = "select BUILD_NAME, AREA_NAME, COUNT(PART_NO) as CNT " +
                " from FullVidhansabha WHERE PART_NO = " + partNo + " and WARD_NO = " + wardNo + " GROUP BY BUILD_NAME,AREA_NAME ";
        Cursor c = mDb.rawQuery(query, null);
        return c;
    }

    public Cursor GetNames_BuildingWise(String BuildingName, String AreaName, String partno) {
        String query = "SELECT LASTNAME, FIRST_NAME, SR_NO,PART_NO,WARD_NO, FIRSTNAME_MARATHI, LASTNAME_MARATHI  FROM FULLVIDHANSABHA WHERE BUILD_NAME = '" + BuildingName + "' AND AREA_NAME = '" + AreaName + "' AND PART_NO = '" + partno + "'";
        Cursor c = mDb.rawQuery(query, null);
        return c;
    }

    public Cursor GetNames_EasySearch(String surname, String name) {
        //or FIRST_NAME Like' " + middleName + "%'
        String query = "SELECT LASTNAME, FIRST_NAME, SR_NO,PART_NO,WARD_NO, FIRSTNAME_MARATHI, LASTNAME_MARATHI  FROM FULLVIDHANSABHA WHERE LASTNAME Like '" + surname + "%' AND FIRST_NAME Like'" + name + "%' order by LASTNAME ASC, FIRST_NAME ASC ";
        Cursor c = mDb.rawQuery(query, null);
        return c;
    }

    //----------Building wise Search In FullVidhansabha-----------------------------------------------------

    public Cursor GetAddress_In_FullVidhansabha(String alpha, String wardNo) {
        String str = "SELECT BUILD_NAME, AREA_NAME, COUNT(PART_NO) FROM " +
                "FULLVIDHANSABHA WHERE BUILD_NAME LIKE '" + alpha + "%' and WARD_NO = "+wardNo+" GROUP BY BUILD_NAME,AREA_NAME";
        Cursor cur = mDb.rawQuery(str, null);
        return cur;
    }

    public Cursor GetAddress_Blank() {
        String query = "select BUILD_NAME, AREA_NAME, COUNT(PART_NO) as CNT from FullVidhansabha WHERE BUILD_NAME not GLOB '[A-Za-z]*' GROUP BY BUILD_NAME,AREA_NAME";
        Cursor c = mDb.rawQuery(query, null);
        return c;
    }

    public Cursor GetNames_In_BlankBuilding(String BuildingName, String AreaName) {
        String query = "SELECT LASTNAME, FIRST_NAME, SR_NO,PART_NO,WARD_NO, FIRSTNAME_MARATHI, LASTNAME_MARATHI  FROM FULLVIDHANSABHA WHERE BUILD_NAME = '" + BuildingName + "' AND AREA_NAME = '" + AreaName + "'";
        Cursor c = mDb.rawQuery(query, null);
        return c;
    }

    public Cursor GetPartNos() {
        String query = "SELECT DISTINCT PART_NO  FROM FULLVIDHANSABHA";
        Cursor c = mDb.rawQuery(query, null);
        return c;
    }

    public Cursor GetDetails_From_Fullvidhansabha_Single_PartNo(String part_no,String SrNo, String wardNo, String filter_type, String age1) {
        String query = "";

        if (filter_type.equals("Surname")) {
            query = "select * from FullVidhansabha where PART_NO = " + part_no +
                    " and WARD_NO = " + wardNo +
                    " order by LASTNAME COLLATE NOCASE ASC";
        } else if (filter_type.equals("Name")) {
            query = "select * from FullVidhansabha where PART_NO = " + part_no +
                    " and WARD_NO = " + wardNo +
                    " order by FIRST_NAME COLLATE NOCASE ASC";
        } else if (filter_type.equals("Building")) {
            query = "select * from FullVidhansabha where PART_NO = " + part_no +
                    " and WARD_NO = " + wardNo +
                    " order by BUILD_NAME COLLATE NOCASE ASC";
        } else if (filter_type.equals("Age")) {
            String[] age = age1.split("-");
            query = "select * from FullVidhansabha where PART_NO = " + part_no +
                    " and WARD_NO = " + wardNo +
                    " and Cast(AGE as integer) between " + age[0] + " and " + age[1] + " order by AGE";
        }

        Cursor c = mDb.rawQuery(query, null);
        return c;
    }

    public Cursor GetDetails_From_Fullvidhansabha_Combine_PartNo(String part_no, String filter_type, String age1) {
        String query = "";

        if (filter_type.equals("Surname")) {
            query = "select * from FullVidhansabha where PART_NO IN (" + part_no + ") order by LASTNAME COLLATE NOCASE ASC";
        } else if (filter_type.equals("Name")) {
            query = "select * from FullVidhansabha where PART_NO IN (" + part_no + ") order by FIRST_NAME COLLATE NOCASE ASC";
        } else if (filter_type.equals("Building")) {
            query = "select * from FullVidhansabha where PART_NO IN (" + part_no + ") order by BUILD_NAME COLLATE NOCASE ASC";
        } else if (filter_type.equals("Age")) {
            String[] age = age1.split("-");
            query = "select * from FullVidhansabha where PART_NO IN (" + part_no + ") and Cast(AGE as integer) between " + age[0] + " and " + age[1] + " order by AGE";
        }

        Cursor c = mDb.rawQuery(query, null);
        return c;
    }

    public Cursor GetDetails_all_From_Fullvidhansabha() {
        String query = "";
        query = "select * from FullVidhansabha order by LASTNAME COLLATE NOCASE ASC";
        Cursor c = mDb.rawQuery(query, null);
        return c;
    }

}
