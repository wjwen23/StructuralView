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
    //�ĵ��������·��
    private static String outputPath = "D:/doc2vec.txt";
    public static void main(String[] args) throws Exception {
        //�����ı��ļ���Ŀ¼
        File inputTxt = new File("D:\\IdeaProjects\\StructrualView\\src\\main\\resources\\test.java");
        log.info("��ʼ��������...."+inputTxt.getName());
        //��������
        SentenceIterator iter = new LineSentenceIterator(inputTxt);
        //�дʲ���
        TokenizerFactory token = new DefaultTokenizerFactory();
        //ȥ��������ż���Сдת������
        token.setTokenPreProcessor(new CommonPreprocessor());
        AbstractCache<VocabWord> cache=new AbstractCache<>();
        //����ĵ���ǩ�����һ����ļ���ȡ��Ϊ�˷���������ʹ��������
        List<String> labelList = new ArrayList<String>();
        for (int i = 0; i < 97162; i++) {
            labelList.add("doc"+i);
        }
        //�����ĵ���ǩ
        LabelsSource source = new LabelsSource(labelList);
        log.info("ѵ��ģ��....");
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
        log.info("���Ƶľ���:");
        Collection<String> lst = vec.wordsNearest("doc0", 10);
        System.out.println(lst);
        log.info("����ĵ�����....");
        WordVectorSerializer.writeWordVectors(vec, outputPath);
        //��ȡĳ�ʶ�Ӧ������
        log.info("������ȡ:");
        double[] docVector = vec.getWordVector("doc0");
        System.out.println(Arrays.toString(docVector));
    }
}
