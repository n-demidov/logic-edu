# Logic-edu

## How to play online

- On `Facebook`: [https://apps.facebook.com/fruit-bounty](https://apps.facebook.com/fruit-bounty) (desktop only)
- On `vk.com`: [https://vk.com/app5154054](https://vk.com/app5154054) (mobile & desktop)
- On `Yandex`: [https://yandex.ru/games/play/178103?draft=true](https://yandex.ru/games/play/178103?draft=true) (no auth necessary)
- https://logic-edu.herokuapp.com/

Or you can search the game in `vk.com` app catalog by key-words like *Fruit's Bounty*.

## How to run locally

1. Set database environment variables (in `application.yml` or by using your IDE): 
- `JDBC_DATABASE_URL`
- `JDBC_DATABASE_USERNAME`
- `JDBC_DATABASE_PASS`

2. Set `Facebook` environment variables. It's needed only for `Facebook` social network requests.
You can skip this step or set dummy/random values if you don't log in via `Facebook`:
- `FACEBOOK_APP_ID` (E.g. `554726609114367` - my dev test appId)

3. Set `vk.com` (`VK`) environment variables. It's needed only for `vk.com` social network requests.
You can skip this step or set dummy/random values if you don't log in via `vk.com`:
- `VK_APP_ID`
- `VK_ACCESS_TOKEN`
- `VK_SECRET_KEY`

4. Run Spring Boot application. And open the game:
- on [http://localhost:5000/fb-app](http://localhost:5000/fb-app) for `Facebook`;
- or open [http://localhost:5000/vk-app?viewer_id=_vk_user_id_&auth_key=_auth_key_](http://localhost:5000/vk-app?viewer_id=_vk_user_id_&auth_key=_auth_key_) for `vk.com`
(you can find `auth_key` in your real `vk` game in `vk` iframe).

You can change the port by set `PORT` environment variable or edit it in `application.yml` file.
By default, the port is `5000`.

Note: you can use Docker to run local PosgreSQL instance:
1. Pull image [https://hub.docker.com/_/postgres/](https://hub.docker.com/_/postgres/)
2. Run docker container, e.g.: ```docker run --name h-postgres -e POSTGRES_PASSWORD=postgres -p:5432:5432 -d postgres:9.6.4```
3. Specify environment variable, e.g.:
   1. `JDBC_DATABASE_URL` = `jdbc:postgresql://192.168.99.100:5432/postgres`
   2. `JDBC_DATABASE_USERNAME` = `postgres`
   3. `JDBC_DATABASE_PASS` = `postgres`

Note: to use Docker on Windows 7 you can setup 'Docker Toolbox' soft. Which will start on specific IP which is shown on startup in a console.

## License
Fruit's Bounty is Open Source software released under the
[Apache 2.0 license](http://www.apache.org/licenses/LICENSE-2.0.html)

Design and images: Irina Pichugina, Elena Pichugina.
Contacts: https://vk.com/i.pichugina99, https://vk.com/id382496812

Developer is Nikita Demidov.

Fonts 'Showcard Gothic' and 'Showcard Gothic Cyrillic':
Copyright: Copyright (c) 1997, 1998 The Font Bureau, Inc. and Jim Parkinson. All rights reserved.
Trademark: Copyright (c) 1985, 1987, 1988, 1989 The Font Bureau, Inc. All rights reserved. Showcard Gothic is a trademark of The Font Bureau, Inc.

## Feedback


## Dates

