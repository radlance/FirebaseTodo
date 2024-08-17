# FireBase Todo

![app](https://camo.githubusercontent.com/323ef51f2f921bd312c333ac04eafb8deb8e1322c43bcae4a3d1e608c20a00a4/68747470733a2f2f696d672e736869656c64732e696f2f7374617469632f76313f7374796c653d666f722d7468652d6261646765266d6573736167653d416e64726f696426636f6c6f723d333441383533266c6f676f3d416e64726f6964266c6f676f436f6c6f723d464646464646266c6162656c3d)


Android-приложение для управления вашими задачами.
---

## Возможности

+ Регистрация
+ Создание и редактирование возникшей задачи
+ Отслеживание общего количества поставленных и выполненных задач
+ Редактирование и удаление профиля

---

## Архитектура

Приложение создано с соблюдением чистой архитектуры,
код разделен на три отдельных уровня:
представление, домен и данные.   
*(Для соблюдения архитектуры в соответствующие уровни были предоставлены необходимые зависимости).*

---

## Используемые технологии

+ **Kotlin (v 1.9.0)** — официально поддерживаемый Google язык
  разработки приложений для Android.

+ **Android Studio (v 2023.3.1)** — официальная интегрированная среда
  разработки (IDE) для разработки приложений Android.


+ **Hilt (v 2.50)** — библиотека от Google,
  используемая для предоставления и внедрения зависимостей.


+ **Coroutines** — компоненты программы,
  которые позволяют выполнить асинхронные вычисления.


+ **MVVM** — архитектурный шаблон,
  используемый для отделения логики пользовательского интерфейса от
  бизнес-логики.


+ **Firebase realtime database** — облачная база данных NoSQL, позволяющая хранить и синхронизировать данные в реальном времени.


+ **Firebase Authentication** — безопасная служба аутентификации и проверки подлинности, предоставляемая платформой Firebase.


+ **Firebase Storage** — облачное хранилище файлов которое позволяет сохранять и загружать файлы в своих приложениях.


![android studio](https://camo.githubusercontent.com/2d397c08eedc8787ef2a85a6a4b391f62d5ef4d89c527e49bc9f3a0b8c54136f/68747470733a2f2f696d672e736869656c64732e696f2f7374617469632f76313f7374796c653d666f722d7468652d6261646765266d6573736167653d416e64726f69642b53747564696f26636f6c6f723d323232323232266c6f676f3d416e64726f69642b53747564696f266c6f676f436f6c6f723d334444433834266c6162656c3d)
![kotlin](https://camo.githubusercontent.com/d3d1086af5c2cc9b242b19407152596a33d4ee77f4c2c76f561ba14a2ee8abe0/68747470733a2f2f696d672e736869656c64732e696f2f7374617469632f76313f7374796c653d666f722d7468652d6261646765266d6573736167653d4b6f746c696e26636f6c6f723d374635324646266c6f676f3d4b6f746c696e266c6f676f436f6c6f723d464646464646266c6162656c3d)
![firebase](https://camo.githubusercontent.com/9c4b110c7977131a59a02a124ac54ceb886350e439fde6da5c845b708eba35f8/68747470733a2f2f696d672e736869656c64732e696f2f7374617469632f76313f7374796c653d666f722d7468652d6261646765266d6573736167653d466972656261736526636f6c6f723d323232323232266c6f676f3d4669726562617365266c6f676f436f6c6f723d464643413238266c6162656c3d)

---

## Экраны
<div align="center">

  ![welcome_screen](screenshots/welcome_screen.jpg)
  ![login_screen](screenshots/login_screen.jpg)
  ![registration_screen](screenshots/registration_screen.jpg)
  ![todo_screen](screenshots/todo_screen.jpg)
  ![todo_editing_screen](screenshots/todo_editing_screen.jpg)
</div>


---

## Установка

Для установки приложения загрузите **[APK-file](https://github.com/radlance/FirebaseTodo/raw/master/app/release/app-release.apk)**
и откройте его на своем устройстве Android.
Альтернативно вы можете
клонировать репозиторий и создать приложение с помощью Android Studio.

```
git clone https://github.com/radlance/FirebaseTodo.git
```
