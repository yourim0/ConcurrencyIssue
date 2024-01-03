## 동시성이슈 해결방안
- 실패 원인 : 레이스 컨디션 발생. 둘 이상의 thread가 공유데이터에 엑세스할 수 있고 동시에 변경을 하려고 할 때 발생한다.
- 해결 방안 : 데이터에 한개의 쓰레드만 접근이 가능하도록 하면 된다.

  **1. synchronized 사용**
    - @Transactional과 동시에 사용했을 때 테스트 실패 원인 : Transactional 어노테이션을 적용하면 만든 클래스를 래핑한 클래스를 새로 만들어서 실행하게 된다.
    - Transaction을 시작 한 후에 메서드를 호출하고, 메서드 실행이 종료가 되면 트랜젝션을 종료하게 된다.
    - 트랜젝션 종료시점에 데이터베이스에 업데이트를 하는데 메서드 완료 후 실제 데이터베이스가 업데이트 되기 전에 다른 스레드가 메서드를 호출할 수 있게된다.
    - 다른 스레드는 갱신 전 값을 가져가 동일한 문제가 발생하게 된다.
  -> @Transactional 제거한다.

    - **synchronized 사용했을 때의 문제점** : 각 프로세스안에서만 적용되기 때문에 1개의 서버에서 갱신중인 시점에 다른 서버에서 갱신되지 않은 데이터를 가져갈 수 있다.
  
    - @Transactional은 주로 데이터베이스의 트렌잭션과 관련된 동시성을 제어하기 위해 사용되며, 데이터베이스 레벨에서 일관성을 유지하는데에 도움이 된다.
    - synchronized는 자바 내부의 동기화 메커니즘으로, 메소드나 코드 블록을 한 시점에 하나의 스레드만 접근할 수 있도록 제한하여 동시성 문제를 해결하고자 할 때 사용된다.


  **2. Database 사용**
  
