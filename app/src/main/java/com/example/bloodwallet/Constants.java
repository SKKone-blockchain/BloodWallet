package com.example.bloodwallet;


import com.klaytn.caver.tx.gas.DefaultGasProvider;
import com.klaytn.caver.utils.ChainId;

import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;

import java.math.BigInteger;

public class Constants {
    public static final String EN_URL = "https://api.baobab.klaytn.net:8651";
    public static final int CHAIN_ID = ChainId.BAOBAB_TESTNET;
    public static final DefaultGasProvider GAS_PROVIDER = new DefaultGasProvider();
    public static final BigInteger GAS_LIMIT = GAS_PROVIDER.getGasLimit();
    public static final BigInteger GAS_PRICE = BigInteger.valueOf(25000000000L);
    public static final DefaultBlockParameter BLOCK_PARAM = DefaultBlockParameterName.LATEST;
    public static final String APP_NAME = "BAPP_COUNT";
    public static final String PRIVATE_KEY = "private_key";
    public static final String SCOPE_BASE_URL = "https://baobab.scope.klaytn.com";
    // For demonstration purpose, we use contract pre-deployed at the following address
    public static final String CONTRACT_ADDRES = "0xd69c2dc05c42866756f6f0e757566b4f5cfcabb0";
    // FYI, the contract we use for this app looks like this:
    /*
        pragma solidity ^0.4.18;
        contract BloodWallet {
            struct Donation {
                address owner;
                string timestamp;
            }
            mapping (string => Donation) donations;
            mapping (address => uint) holding_count;
            function donateCertificate(address _address, string _timestamp, string _code) public {
                Donation storage donation = donations[_code];
                donation.owner = _address;
                donation.timestamp = _timestamp;
                holding_count[_address] += 1;
            }
            function getOwner(string _code) view public returns (address, string) {
                return (donations[_code].owner, donations[_code].timestamp);
            }
            function getHoldingCount(address _address) view public returns (uint) {
                return holding_count[_address];
            }
        }
     */
}
