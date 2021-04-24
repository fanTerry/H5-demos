// see http://vuejs-templates.github.io/webpack for documentation.
//修改config/index.js文件中build参数，这里的参数会在build/webpackage.prod.conf.js中使用到
var path = require('path')
var nowEnv = require('./daily.env')
var nowApiDomain = nowEnv.API_DOMAIN;
console.log(nowApiDomain, 'nowEnv');
module.exports = {
    build: {
        // 添加daily beta prod 三处环境的配制
        prodEnv: require('./prod.env'),
        betaEnv: require('./beta.env'),
        dailyEnv: require('./daily.env'),
        devEnv: require('./dev.env'),
        env: require('./prod.env'),
        index: path.resolve(__dirname, '../dist/index.html'),
        assetsRoot: path.resolve(__dirname, '../dist'),
        assetsSubDirectory: 'public',
        assetsPublicPath: '/',
        productionSourceMap: true,
        productionGzip: false,
        productionGzipExtensions: ['js', 'css']
    },
    dev: {
        env: require('./dev.env'),
        port: 8887,
        proxyTable: {
            "/api": {
                target: nowApiDomain,
                secure: true,
                changeOrigin: true,
                pathRewrite: {
                    '^/api': ''
                }
            },
            "/rsDomain": {
              target: "https://rs.esportzoo.com",
              secure: true,
              changeOrigin: true,
              pathRewrite: {
                '^/rsDomain': ''
            }
          }
        },
        https: true
    }
}
