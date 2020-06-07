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
    // ====================================================
    // TX hash of TX payer has sent to Klaytn
    // ====================================================

    // Notify the client
    res.json({ txhash: hash });
  }).on('receipt', function (receipt) {
    // ====================================================
    // TX has been successfully included in a recent block
    // ====================================================

    // TODO record this event
  }).on('error', function (err, receipt) {
    // Something bad happened
    // In case of an out-of-gas error, the second parameter is the receipt containing the transaction caused the error.
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
