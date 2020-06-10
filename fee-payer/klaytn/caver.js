const Caver = require('caver-js')
const caver = new Caver('https://api.baobab.klaytn.net:8651')

// Set environment variables FEE_PAYER_KEY and FEE_PAYER_ADDRESS before npm start
caver.klay.accounts.wallet.add(process.env.FEE_PAYER_KEY, process.env.FEE_PAYER_ADDRESS);

module.exports = caver;