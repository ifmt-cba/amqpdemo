package br.edu.ifmt.amqpdemo;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.HeadersExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import br.edu.ifmt.amqpdemo.produtor.MessageProducer;

@SpringBootApplication
public class AmqpdemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(AmqpdemoApplication.class, args);
	}

    @Bean
    public RabbitTemplate template(CachingConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }

    @Bean
    public MessageProducer messageProducer(RabbitTemplate template) {
        return new MessageProducer(template);
    }

    /*
    * =====================================================================
    * 								FILAS
    * =====================================================================
    */

    @Bean
    public Queue aliceQueue() {
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("x-dead-letter-exchange", "de.deadletter");
        args.put("x-dead-letter-routing-key", "rk.deadletter");
        args.put("x-message-ttl", 12000);
        return new Queue("q.alice", true, false, false, args);
    }

    @Bean
    public Queue bobQueue() {
        return new Queue("q.bob");
    }

    @Bean
    public Queue carlaQueue() {
        return new Queue("q.carla");
    }

    @Bean
    public Queue invalidQueue() {
        return new Queue("q.invalid");
    }

    @Bean
    public Queue deadLetterQueue() {        
        return new Queue("q.deadletter");
    }

    @Bean
    public Queue quemSouQueue() {        
        return new Queue("q.quemsou");
    }

    /*
    * =====================================================================
    * 					PARA TRATAMENTO DE MENSAGENS INVALIDAS
    * =====================================================================
    */

    @Bean
    public FanoutExchange invalidMessageFanoutExchange() {
        return new FanoutExchange("fe.invalid");
    }

    @Bean
    public Binding invalidFEBinding(Queue invalidQueue, FanoutExchange invalidMessageFanoutExchange) {
        return BindingBuilder
                .bind(invalidQueue)
                .to(invalidMessageFanoutExchange);
    }

    /*
    * =====================================================================
    * 					PARA MENSAGENS VENCIDAS
    * =====================================================================
    */

    @Bean
    public DirectExchange deadLetterDirectExchange() {
        return new DirectExchange("de.deadletter");
    }

    @Bean
    public Binding deadLetterDEBinding(Queue deadLetterQueue, DirectExchange deadLetterDirectExchange) {
        return BindingBuilder
                .bind(deadLetterQueue)
                .to(deadLetterDirectExchange)
                .with("rk.deadletter");
    }

    /*
    * =====================================================================
    * 							DIRECT EXCHANGE
    * =====================================================================
    */

    @Bean
    public DirectExchange messageDirectExchange() {
        DirectExchange de = new DirectExchange("de.message",true, false);
        de.addArgument("alternate-exchange", "fe.invalid");
        return de;
    }

    @Bean
    public DirectExchange aliceDirectExchange() {
        return new DirectExchange("de.alice");
    }

    @Bean
    public Binding aliceDEBinding(Queue aliceQueue, DirectExchange messageDirectExchange) {
        return BindingBuilder
                .bind(aliceQueue)
                .to(messageDirectExchange)
                .with("rk.alice");
    }

    @Bean
    public Binding bobDEBinding(Queue bobQueue, DirectExchange messageDirectExchange) {
        return BindingBuilder
                .bind(bobQueue)
                .to(messageDirectExchange)
                .with("rk.bob");
    }

    @Bean
    public Binding carlaDEBinding(Queue carlaQueue, DirectExchange messageDirectExchange) {
        return BindingBuilder
                .bind(carlaQueue)
                .to(messageDirectExchange)
                .with("rk.carla");
    }

    @Bean
    public Binding quemsouDEBindingAlice(Queue quemSouQueue, DirectExchange aliceDirectExchange) {
        return BindingBuilder
                .bind(quemSouQueue)
                .to(aliceDirectExchange)
                .with("rk.quemsou");
    }

    /*
    * =====================================================================
    * 							FANOUT EXCHANGE
    * =====================================================================
    */

    @Bean
    public FanoutExchange messageFanoutExchange() {
        return new FanoutExchange("fe.message");
    }

    @Bean
    public Binding aliceFEBinding(Queue aliceQueue, FanoutExchange messageFanoutExchange) {
        return BindingBuilder
                .bind(aliceQueue)
                .to(messageFanoutExchange);
    }

    @Bean
    public Binding bobFEBinding(Queue bobQueue, FanoutExchange messageFanoutExchange) {
        return BindingBuilder
                .bind(bobQueue)
                .to(messageFanoutExchange);
    }

    @Bean
    public Binding carlaFEBinding(Queue carlaQueue, FanoutExchange messageFanoutExchange) {
        return BindingBuilder
                .bind(carlaQueue)
                .to(messageFanoutExchange);
    }

    /*
    * =====================================================================
    * 							TOPIC EXCHANGE
    * =====================================================================
    */

    @Bean
    public TopicExchange messageTopicExchange() {
        return new TopicExchange("te.message");
    }

    @Bean
    public Binding aliceTEBinding(Queue aliceQueue, TopicExchange messageTopicExchange) {
        return BindingBuilder.bind(aliceQueue)
                .to(messageTopicExchange)
                .with("#.#.mulher");
    }

    @Bean
    public Binding bobTEBinding(Queue bobQueue, TopicExchange messageTopicExchange) {
        return BindingBuilder.bind(bobQueue)
                .to(messageTopicExchange)
                .with("#.#.homem");
    }

    @Bean
    public Binding carlaTEBinding(Queue carlaQueue, TopicExchange messageTopicExchange) {
        return BindingBuilder.bind(carlaQueue)
                .to(messageTopicExchange)
                .with("#.#.mulher");
    }

    /*
    * =====================================================================
    * 							HEADERS EXCHANGE
    * =====================================================================
    */

    @Bean
    public HeadersExchange messageHeadersExchange() {
        return new HeadersExchange("he.message");
    }

    @Bean
    public Binding aliceHEBinding(Queue aliceQueue,
                                    HeadersExchange messageHeadersExchange) {
        Map<String, Object> propriedades = new HashMap<>();
        propriedades.put("x-match", "all");
        propriedades.put("sexo", "feminino");
        propriedades.put("situacao", "ativo");

        return BindingBuilder
                .bind(aliceQueue)
                .to(messageHeadersExchange)
                .whereAll(propriedades).match();
    }

    @Bean
    public Binding bobHEBinding(Queue bobQueue,
                                    HeadersExchange messageHeadersExchange) {
        Map<String, Object> propriedades = new HashMap<>();
        propriedades.put("x-match", "any");
        propriedades.put("sexo", "masculino");
        propriedades.put("situacao", "aposentado");

        return BindingBuilder
                .bind(bobQueue)
                .to(messageHeadersExchange)
                .whereAny(propriedades).match();
    }

    @Bean
    public Binding carlaHEBinding(Queue carlaQueue,
                                    HeadersExchange messageHeadersExchange) {
        Map<String, Object> propriedades = new HashMap<>();
        propriedades.put("x-match", "all");
        propriedades.put("sexo", "feminino");
        propriedades.put("situacao", "aposentado");

        return BindingBuilder
                .bind(carlaQueue)
                .to(messageHeadersExchange)
                .whereAll(propriedades).match();
    }    

}
