# Конкатенация файлов и разрешение зависимостей

Эта программа предназначена для обработки структуры каталогов, содержащих текстовые файлы и подкаталоги. Она выделяет текстовые файлы, извлекает их содержимое и конкатенирует их в один текстовый файл, разрешая зависимости, указанные директивами внутри файлов.

## Использование

1. Разместите ваши текстовые файлы и каталоги в корневом каталоге files.
2. Запустите программу

## Пример

- Каталог 1
  - Файл 1-1
    ```
    Lorem ipsum dolor sit amet, consectetur adipiscing elit.
    Suspendisse id enim euismod erat elementum cursus.
    In hac habitasse platea dictumst. Etiam vitae tortor ipsum.
    Morbi massa augue, lacinia sed nisl id, congue eleifend lorem.
    require ‘Каталог 2/Файл 2-1’
    Praesent feugiat egestas sem, id luctus lectus dignissim ac.
    Donec elementum rhoncus quam, vitae viverra massa euismod a.
    Morbi dictum sapien sed porta tristique. Donec varius convallis quam in fringilla.
    ```

- Каталог 2
  - Файл 2-1
    ```
    Phasellus eget tellus ac risus iaculis feugiat nec in eros.
    Aenean in luctus ante. In lacinia lectus tempus, rutrum ipsum quis, gravida nunc.
    Fusce tempor eleifend libero at pharetra. Nulla lacinia ante ac felis malesuada auctor.
    Vestibulum eget congue sapien, ac euismod elit. Fusce nisl ante, consequat et imperdiet vel, semper et neque.
    ```

  - Файл 2-2
    ```
    require ‘Каталог 1/Файл 1-1’
    require ‘Каталог 2/Файл 2-1’
    In pretium dictum lacinia. In rutrum, neque a dignissim maximus, dolor mi pretium ante,
    nec volutpat justo dolor non nulla. Vivamus nec suscipit nisl, ornare luctus erat.
    Aliquam eget est orci. Proin orci urna, elementum a nunc ac, fermentum varius eros.
    Mauris id massa elit.
    ```

Программа сгенерирует отсортированный список зависимостей:

1. Каталог 2/Файл 2-1
2. Каталог 1/Файл 1-1
3. Каталог 2/Файл 2-2

Затем она конкатенирует содержимое этих файлов в новый текстовый файл.

## Примечания

- Обнаружение циклических зависимостей приведет к выводу сообщения об ошибке, указывающего на цикл в графе зависимостей.
- Обеспечьте правильное форматирование директив, например, использование одинарных кавычек вокруг путей к файлам.
