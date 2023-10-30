// const { defineConfig } = require('@vue/cli-service')
// module.exports = defineConfig({
//   transpileDependencies: true,
//   lintOnSave: false,
//   devServer: {
//     port: 8088
//   },
// })

const { defineConfig } = require('@vue/cli-service')
const CompressionPlugin = require("compression-webpack-plugin"); // 开启gzip压缩， 按需引用
module.exports = defineConfig({
  transpileDependencies: true,
  // 公共路径(必须有的)
  publicPath: "./",
  productionSourceMap: false,
  transpileDependencies: true,
  lintOnSave: false,
  devServer: {
    port: 8088
  },
  configureWebpack: {
    plugins: [
      new CompressionPlugin({
        algorithm: 'gzip', // 使用gzip压缩
        test: /\.js$|\.html$|\.css$/, // 匹配文件名
        filename: '[path][base].gz[query]', // 压缩后的文件名(保持原文件名，后缀加.gz)
        minRatio: 1, // 压缩率小于1才会压缩
        threshold: 10240, // 对超过10k的数据压缩
        deleteOriginalAssets: false, // 是否删除未压缩的源文件，谨慎设置，如果希望提供非gzip的资源，可不设置或者设置为false（比如删除打包后的gz后还可以加载到原始资源文件）
      }),
    ]
  }
})