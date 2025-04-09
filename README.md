Разработать небольшой веб-сервис с авторизацией и возможностью управления списком книг.

1. Авторизация по логину и паролю (можно in-memory, без регистрации)
2. Добавлять, редактировать, удалять книги
3. Хранить поля: id, vendorCode, title, year, brand, stock, price. Все данные сохраняются в базу данных.
4. Выводить список книг в виде таблицы (Bootstrap+Thymeleaf)
5. Поддерживать пагинацию и фильтрацию по title, brand, year
6. REST API: GET /api/books, POST /api/books, PUT /api/books/{id}, DELETE /api/books/{id}

Приложение запускается на порту 8080
InMemory user details:

login: user

password: password

![img.png](img.png)

При успешном логине нас перекидывает на страницу со всеми книгами

![img_1.png](img_1.png)

фильтрация

![img_2.png](img_2.png)

пагинация

![img_3.png](img_3.png)

Создание новой книги

![img_4.png](img_4.png)

Редактирование книги

![img_5.png](img_5.png)

Удаление книги

![img_6.png](img_6.png)
