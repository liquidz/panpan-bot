# panpan-bot
[![Circle CI](https://circleci.com/gh/liquidz/panpan-bot/tree/master.svg?style=svg)](https://circleci.com/gh/liquidz/panpan-bot/tree/master)
[![Dependency Status](https://www.versioneye.com/user/projects/54e408a2d1ec5734f400023d/badge.svg?style=flat)](https://www.versioneye.com/user/projects/54e408a2d1ec5734f400023d)

## Usage

### On Heroku
```
heroku apps:create FIXME
heroku addons:add rediscloud
git push heroku master
```

```
heroku config:add TZ=Asia/Tokyo
heroku config:add SLACK_OUTGOING_TOKEN=aaa
heroku config:add SLACK_INCOMING_URL=bbb
heroku config:add AWAKE_URL=ccc
```

## License

Copyright (c) 2015 [uochan](http://twitter.com/uochan)

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
