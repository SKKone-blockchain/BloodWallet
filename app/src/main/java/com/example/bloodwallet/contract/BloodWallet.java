package com.example.bloodwallet.contract;


import com.klaytn.caver.Caver;
import com.klaytn.caver.crypto.KlayCredentials;
import com.klaytn.caver.methods.response.KlayTransactionReceipt;
import com.klaytn.caver.tx.SmartContract;
import com.klaytn.caver.tx.manager.TransactionManager;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.tuples.generated.Tuple2;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated smart contract code.
 * <p><strong>Do not modify!</strong>
 */
public class BloodWallet extends SmartContract {
    private static final String BINARY = "608060405234801561001057600080fd5b50610530806100206000396000f3006080604052600436106100565763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416634aaf4a12811461005b57806389e3209914610145578063f3541fee14610178575b600080fd5b34801561006757600080fd5b506040805160206004803580820135601f81018490048402850184019095528484526100b494369492936024939284019190819084018382808284375094975061021f9650505050505050565b6040518083600160a060020a0316600160a060020a0316815260200180602001828103825283818151815260200191508051906020019080838360005b838110156101095781810151838201526020016100f1565b50505050905090810190601f1680156101365780820380516001836020036101000a031916815260200191505b50935050505060405180910390f35b34801561015157600080fd5b50610166600160a060020a036004351661038a565b60408051918252519081900360200190f35b34801561018457600080fd5b5060408051602060046024803582810135601f810185900485028601850190965285855261021d958335600160a060020a031695369560449491939091019190819084018382808284375050604080516020601f89358b018035918201839004830284018301909452808352979a9998810197919650918201945092508291508401838280828437509497506103a59650505050505050565b005b600060606000836040518082805190602001908083835b602083106102555780518252601f199092019160209182019101610236565b51815160209384036101000a60001901801990921691161790529201948552506040519384900381018420548751600160a060020a039091169460009450889350918291908401908083835b602083106102c05780518252601f1990920191602091820191016102a1565b518151600019602094850361010090810a8201928316921993909316919091179092529490920196875260408051978890038201882060019081018054601f60029382161590980290950190941604948501829004820288018201905283875290959450859350840190508282801561037a5780601f1061034f5761010080835404028352916020019161037a565b820191906000526020600020905b81548152906001019060200180831161035d57829003601f168201915b5050505050905091509150915091565b600160a060020a031660009081526001602052604090205490565b600080826040518082805190602001908083835b602083106103d85780518252601f1990920191602091820191016103b9565b51815160209384036101000a6000190180199092169116179052920194855250604051938490038101909320805473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a03891617815586519094506104429360018601935087019150610469565b50505050600160a060020a0316600090815260016020819052604090912080549091019055565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106104aa57805160ff19168380011785556104d7565b828001600101855582156104d7579182015b828111156104d75782518255916020019190600101906104bc565b506104e39291506104e7565b5090565b61050191905b808211156104e357600081556001016104ed565b905600a165627a7a7230582000bb79859ef3b839c3fc13892d12dc7a01d279ebec5eb89b7e35ecea7165dd130029";

    public static final String FUNC_GETOWNER = "getOwner";

    public static final String FUNC_GETHOLDINGCOUNT = "getHoldingCount";

    public static final String FUNC_DONATECERTIFICATE = "donateCertificate";

    protected BloodWallet(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    protected BloodWallet(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, transactionManager, contractGasProvider);
    }

    public RemoteCall<Tuple2<String, String>> getOwner(String _code) {
        final Function function = new Function(FUNC_GETOWNER,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_code)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Utf8String>() {}));
        return new RemoteCall<Tuple2<String, String>>(
                new Callable<Tuple2<String, String>>() {
                    @Override
                    public Tuple2<String, String> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple2<String, String>(
                                (String) results.get(0).getValue(),
                                (String) results.get(1).getValue());
                    }
                });
    }

    public RemoteCall<BigInteger> getHoldingCount(String _address) {
        final Function function = new Function(FUNC_GETHOLDINGCOUNT,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_address)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> donateCertificate(String _address, String _timestamp, String _code) {
        final Function function = new Function(
                FUNC_DONATECERTIFICATE,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_address),
                        new org.web3j.abi.datatypes.Utf8String(_timestamp),
                        new org.web3j.abi.datatypes.Utf8String(_code)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public static BloodWallet load(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return new BloodWallet(contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    public static BloodWallet load(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new BloodWallet(contractAddress, caver, transactionManager, contractGasProvider);
    }

    public static RemoteCall<BloodWallet> deploy(Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(BloodWallet.class, caver, credentials, chainId, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<BloodWallet> deploy(Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(BloodWallet.class, caver, transactionManager, contractGasProvider, BINARY, "");
    }
}