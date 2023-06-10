# CHIP3 | Oracle Web3 Service

CHIP3 is Decentralized Oracle Protocol for acquiring arbitrary
offline info for DeFi usage, providing new possibilities
like algorithm stablecoins, perpetual trades & prediction markets.

Each task consist of composable blocks (Actions) that can
be constructed in a customer web suite (see Constructor).

Supports both precise and medianized
consensus (each task runs separate elections).
Tasks can be made as
run-on-request, as schedules or data feeds with deviation thresholds.

## Demo

- [Video Demo](?)
- [Web3 Demo App](http://chip3.deplant.tech)
- [T-Sol Contracts](https://github.com/deplant/venom-oracle/contracts/src/main/solidity)

## Quick Resources

- [Documentation](http://docs.deplant.tech)
- [Github](https://github.com/deplant/venom-oracle)
- [Support](https://t.me/Laugan)

## Contract Architecture

![contract_architecture.png](contract_architecture.png)

## Tech Resources

- [T-Sol Contracts](https://github.com/deplant/venom-oracle/contracts/src/main/solidity)
- [Contract Deployment Artifacts](https://github.com/deplant/venom-oracle/contracts/src/main/resources/artifacts)
- [JDK Framework Github](https://github.com/deplant/java4ever-framework)

## Connect your contract

- Get [interface for your consumer](https://github.com/deplant/venom-oracle/tree/master/contracts/src/main/solidity/consumer/int)
- Get [interface for your task](https://github.com/deplant/venom-oracle/tree/master/contracts/src/main/solidity/task/int)
- Here is the example of contract for getting answer out of Medianized Feed:

```
pragma ever-solidity ^0.64;

import "./int/IMedianizedConsumer.tsol";
import "../task/int/IMedianizedFeed.tsol";

contract ExampleConsumerTest is IMedianizedConsumer {

    uint128 _medianizedResult;
    address _currentTask;

    constructor() public {
        require(msg.pubkey() == tvm.pubkey(), 199); //NOT_MY_OWNER
        tvm.accept();
    }

    // ****************************************************************
    // Getter
    // ****************************************************************

    function getMedianizedResult() external view returns (uint128) {
        return _medianizedResult;
    }

    // ****************************************************************
    // External
    // ****************************************************************

    function calculateMedianizedFeed(address taskAddress_, address payerAddress_) external {
		// << CALL THIS METHOD TO GET RESPONSE >> 
        require(taskAddress_.value != 0, 444);
        require(msg.pubkey() == tvm.pubkey(), 199); //NOT_MY_OWNER
        tvm.accept();
        _currentTask = taskAddress_;
        IMedianizedFeed(taskAddress_).medianizedFeedRequest{value: 500_000_000, flag: 1, bounce: true}(payerAddress_);
    }

    function medianizedCallback(uint128 response_) override external internalMsg {
        require(msg.sender == _currentTask && msg.sender.value != 0, 198); // NOT_AWAITED_TASK
        _medianizedResult = response_;
		// << INSERT YOUR ACTIONS AFTER ORACLE RESPONSE HERE >> 
    }

}	
```

## Tech Stack

![stack.png](stack.png)

## Tests

### Prerequisites

- [JDK 19](https://adoptium.net/temurin/releases/?version=19)
- [EVER-SDK lib](https://github.com/tonlabs/ever-sdk#download-precompiled-binaries) for your OS
- Set LOCAL_NODE_ENDPOINT environment variable to your EVER OS node
- Set TON_CLIENT_LIB environment variable to EVER-SDK lib path

### How to test oracle contracts

- Clone this repo
- Go to contracts `cd contracts`
- Run `gradlew test --tests "tech.deplant.osiris.contract.test.junit.sequence.TokenSequenceTest" -D--enable-preview -D--add-modules=jdk.incubator.concurrent -D--enable-native-access=ALL-UNNAMED`
- Run `gradlew test --tests "tech.deplant.osiris.contract.test.junit.sequence.BankSequenceTest" -D--enable-preview -D--add-modules=jdk.incubator.concurrent -D--enable-native-access=ALL-UNNAMED`
- Run `gradlew test --tests "tech.deplant.osiris.contract.test.junit.sequence.TaskSequenceTest" -D--enable-preview -D--add-modules=jdk.incubator.concurrent -D--enable-native-access=ALL-UNNAMED`
- Run `gradlew test --tests "tech.deplant.osiris.contract.test.junit.sequence.ValidatorSequenceTest" -D--enable-preview -D--add-modules=jdk.incubator.concurrent -D--enable-native-access=ALL-UNNAMED`
- Run `gradlew test --tests "tech.deplant.osiris.contract.test.junit.sequence.ConsumerSequenceTest" -D--enable-preview -D--add-modules=jdk.incubator.concurrent -D--enable-native-access=ALL-UNNAMED`

