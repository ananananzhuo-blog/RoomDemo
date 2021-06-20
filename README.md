关注公众号学习更多知识

![](https://files.mdnice.com/user/15648/404c2ab2-9a89-40cf-ba1c-02df017a4ae8.jpg)


> 关注我的公众号 **“安安安安卓”** 免费学知识
git地址：

 https://github.com/ananananzhuo-blog/RoomDemo.git

## Room 概述

Room 在 SQLite 上提供了一个抽象层，以便在充分利用 SQLite 的强大功能的同时，能够流畅地访问数据库。

## Room 分析

### Room 中注解和作用

1.  @Database：

@Database 注解的类必须是扩展 RoomDatabase 的抽象类；

包含具有 0 个参数且返回使用 @Dao 注释的类的抽象方法；

可以通过调用 Room.databaseBuilder() 或 Room.inMemoryDatabaseBuilder() 获取 Database 的实例；

2. @Entity

   表示数据库中的表。
3. @Dao

包含用于访问数据库的方法

## Room 使用

### 简单使用

本例我们定义一个Goods的商品，商品包含自增id和商品名称，
然后实现对商品的增加、查询、删除等功能。主要展示商品增加和查询

1. room 依赖

```
dependencies {
    def room_version = "2.3.0"

    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor "androidx.room:room-compiler:$room_version"

    // To use Kotlin annotation processing tool (kapt)
    kapt("androidx.room:room-compiler:$room_version")
    // To use Kotlin Symbolic Processing (KSP)
    ksp("androidx.room:room-compiler:$room_version")

    // optional - Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:$room_version")

    // optional - RxJava2 support for Room
    implementation "androidx.room:room-rxjava2:$room_version"

    // optional - RxJava3 support for Room
    implementation "androidx.room:room-rxjava3:$room_version"

    // optional - Guava support for Room, including Optional and ListenableFuture
    implementation "androidx.room:room-guava:$room_version"

    // optional - Test helpers
    testImplementation("androidx.room:room-testing:$room_version")
}
```
2. 定义RoomDatabase类

```
@Database(entities = arrayOf(Goods::class),version = 1)
abstract class GoodsDatabase: RoomDatabase() {
   abstract fun goodsDao():GoodsDao
}
```
3. 定义Dao类

```
@Dao
interface GoodsDao {
    @Query("SELECT * FROM goods")
    fun getAll(): List<Goods>

    @Query("SELECT * FROM goods WHERE id IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<Goods>

    @Insert
    fun insertAll(vararg users: Goods)

    @Delete
    fun delete(user: Goods)
}
```
4. 定义Goods商品类
```
@Entity
data class Goods(
    @ColumnInfo(name = "goodsName") val name: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long? =null
}
```
因为商品的主键自动累加的，所有不应该在构造方法中传入，所以我们给他初始设置为null，

注：必须设置为null才能实现自增，如果设置为具体值那么就无法自增了
5. 插入数据

```
GlobalScope.launch {
               db.goodsDao().insertAll(Goods("阿娜卡列尼亚"))
           }
```
6. 获取数据并展示

```
GlobalScope.launch {
                val all = db.goodsDao().getAll()
                val sb = StringBuilder()
                for (good in all){
                    sb.append("商品id：${good.id}  商品名：${good.name} \n")
                }
                withContext(Dispatchers.Main){
                    tv_simpleuse.text=sb.toString()
                }
            }
```

7. 展示效果

![](https://files.mdnice.com/user/15648/3a002205-3ec8-49fe-89f9-837c847dc6e8.jpeg)

### 关键知识点
#### Entity
1. @Entity中的字段如果我们不想生成数据库的一列，可以使用@Ignore注解忽略


```
@Ignore val time:Long=0
```
2. 某个类继承了一个父类，让数据库不为父类字段生成列的方法


```
open class User {
        var picture: Bitmap? = null
    }

    @Entity(ignoredColumns = arrayOf("picture"))
    data class RemoteUser(
        @PrimaryKey val id: Int,
        val hasVpn: Boolean
    ) : User()
```
本例中RemoteUser继承了User，User中有一个picture字段，我们不想数据库的表为生成一个picture列，所以在RemoteUser的Entity注解中添加ignoredColumns字段忽略。

3. 如何为实体类中的索引添加顺序

定义实体类的时候注解中加入如下代码：


```
@Entity(indices = arrayOf(Index(value = ["last_name", "address"])))
```

#### Dao （插入查询操作，部分是抄的官网）
##### 如何设置在主线程可以调用数据库查询

构建db对象的时候调用allowMainThreadQueries方法

虽说可以设置，但是建议你100%不要考虑这么做
```
 val db =
            Room.databaseBuilder(applicationContext, GoodsDatabase::class.java, "goods")
                .allowMainThreadQueries()
                .build()
```
##### 插入方法

插入数据的方法可以有一个或多个参数，如果是一个参数可以返回主键，如果是多个参数会返回主键的数组


```
@Insert(onConflict = OnConflictStrategy.REPLACE)
        fun insertUsers(vararg users: User)
```
##### 更新数据

```
@Dao
    interface MyDao {
        @Update
        fun updateUsers(vararg users: User)
    }
```
##### 删除数据

```
@Dao
    interface MyDao {
        @Delete
        fun deleteUsers(vararg users: User)
    }
```
##### 查询

###### 简单查询
```
    @Dao
    interface MyDao {
        @Query("SELECT * FROM user")
        fun loadAllUsers(): Array<User>
    }
```
###### 带查询条件的查询

```
    @Dao
    interface MyDao {
        @Query("SELECT * FROM user WHERE age > :minAge")
        fun loadAllUsersOlderThan(minAge: Int): Array<User>
    }
```

:minAge 绑定参数与 minAge 方法参数进行匹配

###### 返回列表子集

下面代码查询结果会自动赋值到NameTuple中
```
@Dao
    interface MyDao {
        @Query("SELECT first_name, last_name FROM user")
        fun loadFullName(): List<NameTuple>
    }
```

大多数情况下，您只需获取实体的几个字段。例如，您的界面可能仅显示用户的名字和姓氏，而不是用户的每一条详细信息。
###### 查询数量不定的参数

部分查询可能要求您传入数量不定的参数，参数的确切数量要到运行时才知道。例如，您可能希望从部分区域中检索所有用户的相关信息。Room 知道参数何时表示集合，并根据提供的参数数量在运行时自动将其展开。

```
    @Dao
    interface MyDao {
        @Query("SELECT first_name, last_name FROM user WHERE region IN (:regions)")
        fun loadUsersFromRegions(regions: List<String>): List<NameTuple>
    }

```
###### 使用suspend修饰dao中的方法
使用suspend修饰方法，通过协程使这些方法变为异步，避免我们在主线程中进行操作


```
 @Update
        suspend fun updateUsers(vararg users: User)
```

###### 使用Livedata观察数据库操作

```
 @Dao
    interface MyDao {
        @Query("SELECT first_name, last_name FROM user WHERE region IN (:regions)")
        fun loadUsersFromRegionsSync(regions: List<String>): LiveData<List<User>>
    }

```

### 定义对象之间的关系
暂时先不深入这里，回头单开文章讲：

官网参考：

https://developer.android.google.cn/training/data-storage/room/relationships


### 预填数据库
#### 从assets目录预装
如需从位于应用 assets/ 目录中的任意位置的预封装数据库文件预填充 Room 数据库，请先从 RoomDatabase.Builder 对象调用 createFromAsset() 方法，然后再调用 build()


```
 Room.databaseBuilder(appContext, AppDatabase.class, "Sample.db")
        .createFromAsset("database/myapp.db")
        .build()

```
#### 从文件预装

```
    Room.databaseBuilder(appContext, AppDatabase.class, "Sample.db")
        .createFromFile(File("mypath"))
        .build()

```
### 数据库迁移
Room 持久性库支持通过 Migration 类进行增量迁移以满足此需求。每个 Migration 子类通过替换 Migration.migrate() 方法定义 startVersion 和 endVersion 之间的迁移路径。当应用更新需要升级数据库版本时，Room 会从一个或多个 Migration 子类运行 migrate() 方法，以在运行时将数据库迁移到最新版本：


```
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("CREATE TABLE `Fruit` (`id` INTEGER, `name` TEXT, " +
                "PRIMARY KEY(`id`))")
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE Book ADD COLUMN pub_year INTEGER")
    }
}

Room.databaseBuilder(applicationContext, MyDb::class.java, "database-name")
        .addMigrations(MIGRATION_1_2, MIGRATION_2_3).build()
```

官网提供了单元测试的方式测试数据迁移，可以尽量少错误

https://developer.android.google.cn/training/data-storage/room/migrating-db-versions











