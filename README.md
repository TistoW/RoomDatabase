**Android Room Database Library Tutorial**
===================

This is an Android application for demo-ing the Room Database library by Google. Basic database operations with Room & SQLite supported by this tutorial : (CRUD)

* Create
* Read
* Update
* Delete
* Read Single Data

<img src="https://github.com/TistoW/RoomDatabase/blob/main/media/image1.png" width="200" style="max-width:100%;">   <img src="https://github.com/TistoW/RoomDatabase/blob/main/media/image2.png" width="200" style="max-width:100%;"></br>

Step 1: Add following library and annotation processor to your app gradle file.

    //SQLite
    kapt "android.arch.persistence.room:compiler:1.1.1"
    implementation 'android.arch.persistence.room:runtime:1.1.1'
    implementation 'io.reactivex.rxjava2:rxandroid:2.0.2'
    implementation "androidx.room:room-runtime:2.2.5"
    annotationProcessor "androidx.room:room-compiler:2.2.5"

Step 2: Component 1 in room - Create an entity class / Model:

This is nothing but a model class annotated with @Entity where all the variable will becomes column name for the table and name of the model class becomes name of the table. The name of the class is the table name and the variables are the columns of the table

Example: NoteModel.kt

    @Entity(tableName = "note") // the name of tabel
    class NoteModel {

        @PrimaryKey(autoGenerate = true) 
        @ColumnInfo(name = "id")
        var id: Int? = null

        lateinit var title: String
        lateinit var description: String
        lateinit var time: String
    }

Step 3: Component 2 in room - Create a DAO class

This is an interface which acts is an intermediary between the user and the database. All the operation to be performed on a table has to be defined here. We define the list of operation that we would like to perform on table

Example: DaoNote.kt

    @Dao
    interface DaoNote {

        @Insert(onConflict = REPLACE)
        fun insert(data: NoteModel)

        @Delete
        fun delete(data: NoteModel)

        @Update
        fun update(data: NoteModel): Int

        @Query("SELECT * from note ORDER BY id ASC")
        fun getAll(): List<NoteModel>

        @Query("SELECT * FROM note WHERE id = :id LIMIT 1")
        fun getNote(id: Int): NoteModel

        @Query("DELETE FROM note")
        fun deleteAll(): Int
    }

Step 4: Component 3 in room - Create Database class

This is an abstract class where you define all the entities that means all the tables that you want to create for that database. We define the list of operation that we would like to perform on table

Example: MyDatabase.kt

    @Database(entities = [NoteModel::class] /* List model Ex:NoteModel */, version = 1)  
    abstract class MyDatabase : RoomDatabase() {
        abstract fun daoNote(): DaoNote // DaoNote

        companion object {
            private var INSTANCE: MyDatabase? = null

            fun getInstance(context: Context): MyDatabase? {
                if (INSTANCE == null) {
                    synchronized(MyDatabase::class) {
                        INSTANCE = Room.databaseBuilder(
                                context.applicationContext,
                                MyDatabase::class.java, "MyDatabaseName" // Database Name
                        ).allowMainThreadQueries().build()
                    }
                }
                return INSTANCE
            }

            fun destroyInstance() {
                INSTANCE = null
            }
        }
    }

**Note: DO NOT PERFORM OPERATION ON MAIN THREAD AS APP WILL CRASH**

Sample Implementation of basic CRUD operations using ROOM

**1. Insert:**

    val myDb: MyDatabase = MyDatabase.getInstance(this) // call database
    val note = NoteModel() //create new note
    note.title = "First Note"
    note.description = "Note Description"

    CompositeDisposable().add(Observable.fromCallable { myDb.daoNote().insert(note) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Log.d("respons", "data inserted")
            })

**2. Update:**

    val myDb: MyDatabase = MyDatabase.getInstance(this) // call database
    val note = myDb.daoNote().getNote(1) // get note
    note.title  = "Edite Titel"
    note.description = "this is the new description"

    CompositeDisposable().add(Observable.fromCallable { myDb.daoNote().update(note) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Log.d("respons", "data updated")
            })

**3. Delete:**

    val myDb: MyDatabase = MyDatabase.getInstance(this) // call database
    val note = myDb.daoNote().getNote(1)// get note

    CompositeDisposable().add(Observable.fromCallable { myDb.daoNote().delete(note) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Log.d("respons", "data deleted")
            })

**4. Get all notes:**

    val myDb: MyDatabase = MyDatabase.getInstance(this) // call database
    val listNote = myDb.daoNote().getAll() // get All data
    for(note :NoteModel in listNote){
        println("-----------------------")
        println(note.title)
        println(note.description)
    }

**5. Get single note by id:**

    val myDb: MyDatabase = MyDatabase.getInstance(this) // call database
    val note = myDb.daoNote().getNote(1)// get note

And that's it! It's super simple. You can check out the official documentation <a href="https://developer.android.com/topic/libraries/architecture/room" target="_blank">here</a>
