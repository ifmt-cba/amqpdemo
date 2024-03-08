package br.edu.ifmt.amqpdemo.consumidor;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class Alice {

    @RabbitListener(queues = "q.alice")
    private void recebeMensagem(String msg) {
        System.out.println("Alice recebeu: " + msg);
    }

}
