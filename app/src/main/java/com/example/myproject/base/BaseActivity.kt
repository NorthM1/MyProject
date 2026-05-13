import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    private var _binding: VB? = null

    // 供子类使用的只读属性
    protected val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. 让子类自己提供 binding 实例
        _binding = getViewBinding(layoutInflater)

        // 2. 设置根视图
        setContentView(binding.root)

        initView()
        initData()
    }

    /**
     * 抽象方法：子类必须实现，并返回对应的 Binding 对象
     */
    protected abstract fun getViewBinding(inflater: LayoutInflater): VB

    abstract fun initView()
    abstract fun initData()

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    protected fun LogD(str:String){
        Log.d("log",str)
    }
}