package com.jiwen.lexicalview;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.paragraphvectors.ParagraphVectors;
import org.deeplearning4j.models.word2vec.VocabWord;
import org.deeplearning4j.models.word2vec.wordstore.inmemory.AbstractCache;
import org.deeplearning4j.text.documentiterator.LabelsSource;
import org.deeplearning4j.text.sentenceiterator.LineSentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


public class test {
    private static Logger log = LoggerFactory.getLogger(Word2Vector.class);
    //文档向量输出路径
    private static String outputPath = "D:/doc2vec.txt";
    public static void main(String[] args) throws Exception {
        //输入文本文件的目录
        File inputTxt = new File("D:\\IdeaProjects\\StructrualView\\src\\main\\resources\\test.java");
        log.info("开始加载数据...."+inputTxt.getName());
        //加载数据
        SentenceIterator iter = new LineSentenceIterator(inputTxt);
        //切词操作
        TokenizerFactory token = new DefaultTokenizerFactory();
        //去除特殊符号及大小写转换操作
        token.setTokenPreProcessor(new CommonPreprocessor());
        AbstractCache<VocabWord> cache=new AbstractCache<>();
        //添加文档标签，这个一般从文件读取，为了方面我这里使用了数字
        List<String> labelList = new ArrayList<String>();
        for (int i = 0; i < 97162; i++) {
            labelList.add("doc"+i);
        }
        //设置文档标签
        LabelsSource source = new LabelsSource(labelList);
        log.info("训练模型....");
        ParagraphVectors vec = new ParagraphVectors.Builder()
                .minWordFrequency(1)
                .iterations(5)
                .epochs(1)
                .layerSize(200)
                .learningRate(0.025)
                .labelsSource(source)
                .windowSize(5)
                .iterate(iter)
                .trainWordVectors(false)
                .vocabCache(cache)
                .tokenizerFactory(token)
                .sampling(0)
                .build();

        vec.fit();
        log.info("相似的句子:");
        Collection<String> lst = vec.wordsNearest("doc0", 10);
        System.out.println(lst);
        log.info("输出文档向量....");
        WordVectorSerializer.writeWordVectors(vec, outputPath);
        //获取某词对应的向量
        log.info("向量获取:");
        double[] docVector = vec.getWordVector("doc0");
        System.out.println(Arrays.toString(docVector));
    }
}
