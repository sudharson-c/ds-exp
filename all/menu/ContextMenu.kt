
import android.graphics.Color
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
class MainActivity : AppCompatActivity() {
    private lateinit var imageView:ImageView
    private lateinit var mainLayout:ConstraintLayout
    private lateinit var textView:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        imageView=findViewById(R.id.image1)
        mainLayout=findViewById(R.id.main)
        imageView.setOnCreateContextMenuListener(this)
    }
    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?)
    {
        super.onCreateContextMenu(menu, v, menuInfo)
        menu?.setHeaderTitle("CHOOSE BACKGROUND COLOUR");
        menu?.add(0,101,0,"GREY")
        menu?.add(0,102,0,"RED");
    }
    override fun onContextItemSelected(item:MenuItem):Boolean{
        when(item.title.toString())
        {
            "GREY"->mainLayout.setBackgroundColor(Color.GRAY);
            "RED"->mainLayout.setBackgroundColor(Color.RED);
            else->return super.onContextItemSelected(item)
        }
        return true}}
