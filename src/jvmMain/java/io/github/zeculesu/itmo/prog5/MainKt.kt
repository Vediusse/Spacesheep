import io.github.zeculesu.itmo.prog5.GUI.Windows.SignUp
import io.github.zeculesu.itmo.prog5.GUI.UDPGui
import io.github.zeculesu.itmo.prog5.GUI.Windows.LogIn
import io.github.zeculesu.itmo.prog5.GUI.Windows.Main
import javafx.application.Application

fun main(argv: Array<String>) {
    // Создайте объект UDPClient с указанными хостом и портом
    val host = "localhost"
    val port = 45000
    val udpClient = UDPGui(host, port)


    // Запустите приложение
    Application.launch(Main::class.java, *argv)
}
