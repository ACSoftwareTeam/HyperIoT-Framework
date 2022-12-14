package it.acsoftware.hyperiot.blockchain.ethereum.connector.api;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 * @Author Aristide Cittadino.
 * This interface maps the concept of ethereum client.
 * It tries to generalize and standardize some ethereum blockchain methods in order to be independent from client technology.
 * So far there's only one type of client based on web3j.
 */
public interface EthereumClient {
    /**
     * Method for transfering ether between accounts.
     * The source account is the one chosen by setting client credentials
     *
     * @param destination Destination address
     * @param amount      ETHER amount
     * @return
     * @throws Exception
     */
    EthereumTransactionReceipt transferEther(String destination, BigDecimal amount) throws Exception;

    List<String> listAccounts();

    /**
     * Retrieves the balance of an account based on the last block
     *
     * @param address account address
     * @return balance until the last block
     */
    BigInteger getBalanceOf(String address);

    /**
     * Set client credentials. It determine the account used for transactions.
     *
     * @param username username
     * @param password password
     */
    void setCredentials(String username, String password);

    void setCredentials(String privateKey);
}
