# Payer Express
Payer Express is a simple example demonstrating fee delegation on Klaytn.

## How to Use
Payer Express uses Node.js and Express. Clone this repository and run `npm install` to install required dependencies.
```
$ npm install
```

Executing Payer Express requires two environment variables: `FEE_PAYER_KEY` and `FEE_PAYER_ADDRESS`. 

`FEE_PAYER_KEY` is your **private key** which can control the account where the fees are charged, and `FEE_PAYER_ADDRESS` is **the address bound to the account**. Since it is possible that there is no cryptographical relationship between `FEE_PAYER_KEY` and `FEE_PAYER_ADDRESS`, Payer Express requires both explicitly. For more information, please refer [this](https://docs.klaytn.com/klaytn/design/accounts#klaytn-accounts).

Set the required environment variables using your preferred method. For Linux, the following will suit the needs.
```
$ export FEE_PAYER_KEY=0xABCD1234...56EF
$ export FEE_PAYER_ADDRESS=0xCDBEF...1234
```

Once you are ready, run Payer Express using the following command:
```
$ npm start
```

By default, Payer Express runs on `localhost:3000`.