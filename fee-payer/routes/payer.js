var caver = require('../klaytn/caver');
var express = require('express');
var router = express.Router();

// Assume reqBody is a JSON object containing a single field: `senderRawTX`
runFeePayer = (reqBody, res) => {
  feePayer = caver.klay.accounts.wallet[0];
  // Sign and send with `feePayer`
  caver.klay.sendTransaction({
    senderRawTransaction: reqBody.senderRawTX,
    feePayer: feePayer.address
  }).on('transactionHash', function (hash) {


    // Notify the client
    res.json({ txhash: hash });
  }).on('receipt', function (receipt) {

  }).on('error', function (err, receipt) {
    // Something bad happened
    res.json({
      txhash: '',
      error: err.message
    });
    console.error(err);
  });
}

router.post('/', function (req, res) {
  runFeePayer(req.body, res);
});

module.exports = router;
