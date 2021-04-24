var merge = require('webpack-merge')
var prodEnv = require('./prod.env')

module.exports = merge(prodEnv, {
  NODE_ENV: '"development"',
  EVN_CONFIG: '"dev"',
  API_DOMAIN:'https://daily-api.fyesport.com',
  RES_DOMAIN:'https://daily-rs.esportzoo.com',
})
