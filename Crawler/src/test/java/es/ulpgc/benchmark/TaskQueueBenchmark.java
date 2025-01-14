package es.ulpgc.benchmark;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.collection.IQueue;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.config.Config;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

@State(org.openjdk.jmh.annotations.Scope.Thread)
public class TaskQueueBenchmark {

    private IQueue<Integer> taskQueue;

    @Setup
    public void setup() {
        // Configurar e inicializar Hazelcast
        Config config = new Config();
        HazelcastInstance instance = Hazelcast.newHazelcastInstance(config);

        // Obtener la instancia de Hazelcast y la cola de tareas
        taskQueue = instance.getQueue("taskQueue");
    }

    @Benchmark
    public void benchmarkTaskQueueAdd() {
        // Benchmark para agregar un elemento a la cola
        taskQueue.offer(1); // Agregar tarea con ID 1
    }

    @Benchmark
    public void benchmarkTaskQueuePoll() {
        // Benchmark para extraer un elemento de la cola
        taskQueue.poll(); // Extraer el siguiente elemento de la cola
    }
}
