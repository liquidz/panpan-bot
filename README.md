# panpan-bot

[![Circle CI](https://circleci.com/gh/liquidz/panpan-bot/tree/master.svg?style=svg)](https://circleci.com/gh/liquidz/panpan-bot/tree/master) [![Dependency Status](https://www.versioneye.com/user/projects/54e408a2d1ec5734f400023d/badge.svg?style=flat)](https://www.versioneye.com/user/projects/54e408a2d1ec5734f400023d)

## Usage

```
beco -i -c heroku /heroku_login.sh
beco heroku apps:create panpan-bot
beco heroku addons:add rediscloud
beco -c heroku git push heroku master

beco heroku config:add TZ=Asia/Tokyo
beco heroku config:add SLACK_OUTGOING_TOKEN=aaa
beco heroku config:add SLACK_INCOMING_URL=bbb
beco heroku config:add AWAKE_URL=ccc
```

## License

Copyright (c) 2015 [@uochan](http://twitter.com/uochan)

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
