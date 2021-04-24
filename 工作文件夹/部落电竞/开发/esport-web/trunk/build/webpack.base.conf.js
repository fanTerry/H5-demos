var path = require('path')
var config = require('../config')
var utils = require('./utils')
var ExtractTextPlugin = require('extract-text-webpack-plugin')
var vueLoaderConfig = require('./vue-loader.conf')
var webpack = require('webpack')
const CopyWebpackPlugin = require('copy-webpack-plugin');


function resolve(dir) {
    return path.join(__dirname, '..', dir)
}

module.exports = {
    entry: {
        app: ["babel-polyfill", "./src/main.js"]
        // app: './src/main.js'
    },
    output: {
        path: config.build.assetsRoot,
        publicPath: config.build.assetsPublicPath,
        filename: '[name].js'
    },
    resolve: {
        extensions: ['.css', '.js', '.vue'],
        // fallback: [path.join(__dirname, '../node_modules')],
        alias: {
            'vue$': 'vue/dist/vue',
            'src': path.resolve(__dirname, '../src'),
            'assets': path.resolve(__dirname, '../src/assets'),
            'components': path.resolve(__dirname, '../src/components'),
            'libs': path.resolve(__dirname, '../src/libs'),
        }
    },
    module: {
        rules: [
            // {
            //     test: /\.(js|vue)$/,
            //     loader: 'eslint-loader',
            //     enforce: "pre",
            //     include: [resolve('src'), resolve('test')],
            //     options: {
            //         formatter: require('eslint-friendly-formatter')
            //     }
            // },
            {
                test: /\.vue$/,
                loader: 'vue-loader',
                options: vueLoaderConfig
            },
            {
                test: /\.js$/,
                loader: 'babel-loader',
                include: [resolve('src'), resolve('test')]
            },
            {
                test: /\.(png|jpe?g|gif|svg)(\?.*)?$/,
                loader: 'url-loader',
                query: {
                    limit: 10000,
                    name: utils.assetsPath('img/[name].[hash:7].[ext]')
                }
            },
            {
                test: /\.(woff2?|eot|ttf|otf)(\?.*)?$/,
                loader: 'url-loader',
                query: {
                    limit: 10000,
                    name: utils.assetsPath('fonts/[name].[hash:7].[ext]')
                }
            },
            {
                test: /\.s[a|c]ss$/,
                loader: ["style-loader", "css-loader", "sass-loader"]
            },
            {
                test: /\.css$/,
                loader: ["style-loader", "css-loader", "sass-loader"]
            }
            // {
            //     test: /\.scss$/,
            //     include: [resolve('src'), resolve('test')],
            //     exclude: [/node_modules\/(?!(ng2-.+|ngx-.+))/],
            //     use: ExtractTextPlugin.extract({
            //         use: ['css-loader', 'sass-loader'],
            //         fallback: 'style-loader'
            //     })
            // },
            // {
            //     test: /\.css$/,
            //     include: [resolve('src'), resolve('test')],
            //     exclude: [/node_modules\/(?!(ng2-.+|ngx-.+))/],
            //     use: ExtractTextPlugin.extract({
            //         use: ['css-loader'],
            //         fallback: 'style-loader'
            //     })
            // },
            // {
            //     test: /\.scss|css$/
            // }
        ]
    },
    plugins: [
        new webpack.optimize.CommonsChunkPlugin('common.js'),
        new webpack.ProvidePlugin({
            jQuery: "jquery", $: "jquery"
        }),
        new CopyWebpackPlugin([
            {
                from: path.join(__dirname, "../static/MP_verify_4chSe1QeGcpuk8AJ.txt"),
                to: path.join(__dirname, "../MP_verify_4chSe1QeGcpuk8AJ.txt")
            },
            {
                from: path.join(__dirname, "../static"),
                to: path.join(__dirname, "../static")
            }
        
        ])

    ]
}