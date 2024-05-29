import io.github.zeculesu.itmo.prog5.GUI.Windows.SignUp
import io.github.zeculesu.itmo.prog5.GUI.UDPGui
import javafx.application.Application

fun main(argv: Array<String>) {
    // Создайте объект UDPClient с указанными хостом и портом
    val host = "localhost"
    val port = 45000
    val udpClient = UDPGui(host, port)

    udpClient.createSocket()

    // Передайте объект UDPClient в приложение
    SignUp.setUdpClient(udpClient)

    // Запустите приложение
    Application.launch(SignUp::class.java, *argv)
}
