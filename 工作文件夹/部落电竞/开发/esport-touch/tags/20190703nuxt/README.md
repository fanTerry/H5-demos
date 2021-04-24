#基于vue.js开发的橘子电竞webapp [线上访问地址](https://m.esportzoo.com)

通过`npm`安装本地服务第三方依赖模块(需要已安装[Node.js](https://nodejs.org/))

```
npm install
```

启动服务(http://localhost:8888)

```
npm run dev
```

发布代码
```
npm run build
```

###开发

###目录结构

└─esport-touch
  ├─.nuxt               // Nuxt自动生成，临时的用于编辑的文件，build
  ├─assets              // 用于组织未编译的静态资源如LESS、SASS或JavaScript,对于不需要通过 Webpack 处理的静态资源文件，可以放置在 static 目录中
  ├─components          // 用于自己编写的Vue组件，比如日历组件、分页组件
  ├─layouts             // 布局目录，用于组织应用的布局组件，不可更改⭐
  ├─middleware          // 用于存放中间件
  ├─node_modules
  ├─pages               // 用于组织应用的路由及视图,Nuxt.js根据该目录结构自动生成对应的路由配置，文件名不可更改⭐
  ├─plugins             // 用于组织那些需要在 根vue.js应用 实例化之前需要运行的 Javascript 插件。
  ├─static              // 用于存放应用的静态文件，此类文件不会被 Nuxt.js 调用 Webpack 进行构建编译处理。 服务器启动的时候，该目录下的文件会映射至                          应用的根路径 / 下。文件夹名不可更改。⭐
  └─store               // 用于组织应用的Vuex 状态管理。文件夹名不可更改。⭐
  ├─.editorconfig       // 开发工具格式配置
  ├─.eslintrc.js        // ESLint的配置文件，用于检查代码格式
  ├─.gitignore          // 配置git忽略文件
  ├─nuxt.config.js      // 用于组织Nuxt.js 应用的个性化配置，以便覆盖默认配置。文件名不可更改。⭐
  ├─package-lock.json   // npm自动生成，用于帮助package的统一设置的，yarn也有相同的操作
  ├─package.json        // npm 包管理配置文件
  ├─README.md

