package io.github.zeculesu.itmo.prog5.server.net;

import io.github.zeculesu.itmo.prog5.client.ConsoleCommandEnvironment;
import io.github.zeculesu.itmo.prog5.data.AuthCheckSpaceMarineCollection;
import io.github.zeculesu.itmo.prog5.data.CachedSpaceMarineCollection;
import io.github.zeculesu.itmo.prog5.data.InMemorySpaceMarineCollection;
import io.github.zeculesu.itmo.prog5.models.Request;
import io.github.zeculesu.itmo.prog5.models.Response;
import io.github.zeculesu.itmo.prog5.models.SpaceMarine;
import io.github.zeculesu.itmo.prog5.sql.JDBCSpaceMarineCollection;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.*;

import static kotlin.io.ConsoleKt.readlnOrNull;

public class Server {
    private final int port;
    private final ConsoleCommandEnvironment environment;

    protected static CachedSpaceMarineCollection cachedSpaceMarineCollection;
    private static final Map<String, AuthCheckSpaceMarineCollection> clientCollections = new HashMap<>();

    private final byte[] receiveData = new byte[65507];

    public Server(ConsoleCommandEnvironment environment, int port) {
        this.environment = environment;
        this.port = port;
    }

    public void start() {
        System.out.println("Начало работы сервера");
        this.environment.setRun(true);

        System.out.println("-------");

        if (!connectDB()) {
            return;
        }
        System.out.println("-------");

        createCollection();
        System.out.println("-------");
        run();
        System.exit(0);
    }

//    public void run() {
//        try {
//            // Создаем сокет для приема данных на порту
//            DatagramSocket serverSocket = new DatagramSocket(port);
//            ExecutorService readingService = Executors.newFixedThreadPool(4);
//            ExecutorService processingService = Executors.newFixedThreadPool(4);
//            Queue<Future<Request>> readRequestQueue = new ConcurrentLinkedQueue<>();
//            Queue<Request> processRequestQueue = new ConcurrentLinkedQueue<>();
//            Queue<Future<Response>> responseQueue = new ConcurrentLinkedQueue<>();
//
//
//            // получаем запрос от клиента
//            DatagramPacket receivePacket = ConnectionReception.reception(serverSocket, this.receiveData);
//
//            Runnable requestReader = () -> {
//                while (this.environment.isRun()) {
//                    try {
//                        Callable<Request> readedRequest = () -> RequestReading.requestRead(receivePacket);
//                        Future<Request> result = readingService.submit(readedRequest);
//                        readRequestQueue.add(result);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            };
//
//            Runnable requestProcessor = () -> {
//                while (this.environment.isRun()) {
//                    try {
//                        Future<Request> futureRequest = readRequestQueue.poll();
//                        if (futureRequest != null && futureRequest.isDone()) {
//                            Request request = futureRequest.get();
//                            processRequestQueue.add(request);
//                        }
//                        Request request = processRequestQueue.poll();
//                        if (request != null) {
//                            Future<Response> resp = (Future<Response>) processingService.submit(() -> {
//                                RequestExecute.requestExecute(environment, clientCollections, request);
//                            });
//                            responseQueue.add(resp);
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            };
//
//            Runnable responseSender = () -> {
//                while (this.environment.isRun()) {
//                    try {
//                        Future<Response> futureResponse = responseQueue.poll();
//                        if (futureResponse != null && futureResponse.isDone()) {
//                            Response response = futureResponse.get();
//                            //отправляем ответ клиенту
//                            Runnable sendingTask = () -> {
//                                try {
//                                    ResponseSending.responseSend(serverSocket, receivePacket, response);
//                                } catch (IOException e) {
//                                    throw new RuntimeException(e);
//                                }
//                            };
//                            Thread thread = new Thread(sendingTask);
//                            thread.start();
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            };
//
//            // Запускаем потоки чтения и обработки запросов
//            Thread readerThread = new Thread(requestReader);
//            Thread processorThread = new Thread(requestProcessor);
//            Thread senderThread = new Thread(responseSender);
//
//            readerThread.start();
//            processorThread.start();
//            senderThread.start();
//
//            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
//                readingService.shutdown();
//                processingService.shutdown();
//                try {
//                    if (!readingService.awaitTermination(60, TimeUnit.SECONDS)) {
//                        readingService.shutdownNow();
//                    }
//                    if (!processingService.awaitTermination(60, TimeUnit.SECONDS)) {
//                        processingService.shutdownNow();
//                    }
//                } catch (InterruptedException ex) {
//                    readingService.shutdownNow();
//                    processingService.shutdownNow();
//                    Thread.currentThread().interrupt();
//                }
//                serverSocket.close();
//            }));
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//    }

    public void run() {
        try {
            DatagramSocket serverSocket = new DatagramSocket(port);
            ExecutorService executorService = Executors.newFixedThreadPool(4);
            while (this.environment.isRun()) {
                DatagramPacket receivePacket = ConnectionReception.reception(serverSocket, this.receiveData);

                CompletableFuture.supplyAsync(() -> {
                            try {
                                return RequestReading.requestRead(receivePacket);
                            } catch (IOException | ClassNotFoundException ignored) {
                            }
                            return null;
                        }, executorService)
                        .thenApplyAsync(requestData -> RequestExecute.requestExecute(this.environment, clientCollections, requestData), executorService)
                        .thenAcceptAsync(response -> {
                            Runnable sendingTesk = () -> {
                                try {
                                    ResponseSending.responseSend(serverSocket, receivePacket, response);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            };
                            Thread thread = new Thread(sendingTesk);
                            thread.start();
                        })
                        .exceptionally(ex -> {
                            ex.printStackTrace();
                            return null;
                        });
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private boolean connectDB() {
        try {
            System.out.println("ПОДКЛЮЧЕНИЕ К БАЗЕ ДАННЫХ");
            System.out.print("Введите имя пользователя: ");
            String username = readlnOrNull();
            System.out.print("Введите пароль: ");
            String password = readlnOrNull();
            System.out.print("Введите url (jdbc:postgresql://localhost:5432/SpaceMarines): ");
            String url = readlnOrNull();
            environment.setConnection(url, username, password);
            if (!environment.getConnection().connect().isValid(5)) {
                environment.getConnection().connect().close();
                return false;
            }
            environment.getConnection().connect().close();
            System.out.println("База данных подключена");
            return true;
        } catch (SQLException e) {
            //todo System.out.println(e.getMessage());
            System.out.println("Не удалось получить доступ к бд");
            System.out.println("Проверьте, что с базой данной всё в порядке и перезапустите сервер");
            return false;
        }
    }

    private void createCollection() {
        System.out.println("ИНИЦИАЛИЗАЦИЯ КОЛЛЕКЦИИ");
        JDBCSpaceMarineCollection jdbcSpaceMarineCollection = new JDBCSpaceMarineCollection(environment.getConnection());
        System.out.print("Надо создать таблицы заново (у/n): ");
        if (Objects.equals(readlnOrNull(), "y")) {
            jdbcSpaceMarineCollection.initDB();
            System.out.println("Таблицы созданы");
        }

        InMemorySpaceMarineCollection inMemorySpaceMarineCollection = new InMemorySpaceMarineCollection();

        for (SpaceMarine o : jdbcSpaceMarineCollection.show()) {
            inMemorySpaceMarineCollection.add(o.getId(), o);
        }

        cachedSpaceMarineCollection = new CachedSpaceMarineCollection(jdbcSpaceMarineCollection, inMemorySpaceMarineCollection);

        //   df = AuthCheckSpaceMarineCollection(new CachedSpaceMarineCollection(inMemorySpaceMarineCollection, jdbcSpaceMarineCollection), "login");
    }
}
