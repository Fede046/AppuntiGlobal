JAVA_FILES := $(wildcard *.java)
JAVA_CLASSES := $(patsubst %.java, %.class, $(JAVA_FILES))

ALL: $(JAVA_CLASSES)

.PHONY: clean

Prim.class: Prim.java MinHeap.java

Kruskal.class: Kruskal.java

MST.class: MST.java

MinHeap.class: MinHeap.java

%.class: %.java
	javac $?

clean:
	\rm -f *.class *~
