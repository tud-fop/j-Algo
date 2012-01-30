<object-stream>
  <org.jalgo.module.em.data.StartParameters>
    <events>
      <org.jalgo.module.em.data.Event>
        <tuple>
          <int>1</int>
          <int>1</int>
        </tuple>
        <experimentVector>
          <int>2</int>
          <int>2</int>
        </experimentVector>
        <yield>
          <name>0</name>
          <frequency>4.0</frequency>
          <elements class="sorted-set">
            <org.jalgo.module.em.data.Event reference="../../.."/>
          </elements>
        </yield>
      </org.jalgo.module.em.data.Event>
      <org.jalgo.module.em.data.Event>
        <tuple>
          <int>2</int>
          <int>1</int>
        </tuple>
        <experimentVector reference="../../org.jalgo.module.em.data.Event/experimentVector"/>
        <yield>
          <name>1</name>
          <frequency>9.0</frequency>
          <elements class="sorted-set">
            <org.jalgo.module.em.data.Event>
              <tuple>
                <int>1</int>
                <int>2</int>
              </tuple>
              <experimentVector reference="../../../../../org.jalgo.module.em.data.Event/experimentVector"/>
              <yield reference="../../.."/>
            </org.jalgo.module.em.data.Event>
            <org.jalgo.module.em.data.Event reference="../../.."/>
          </elements>
        </yield>
      </org.jalgo.module.em.data.Event>
      <org.jalgo.module.em.data.Event>
        <tuple>
          <int>2</int>
          <int>2</int>
        </tuple>
        <experimentVector reference="../../org.jalgo.module.em.data.Event/experimentVector"/>
        <yield>
          <name>2</name>
          <frequency>2.0</frequency>
          <elements class="sorted-set">
            <org.jalgo.module.em.data.Event reference="../../.."/>
          </elements>
        </yield>
      </org.jalgo.module.em.data.Event>
      <org.jalgo.module.em.data.Event reference="../org.jalgo.module.em.data.Event[2]/yield/elements/org.jalgo.module.em.data.Event"/>
    </events>
    <observations>
      <org.jalgo.module.em.data.Partition reference="../../events/org.jalgo.module.em.data.Event/yield"/>
      <org.jalgo.module.em.data.Partition reference="../../events/org.jalgo.module.em.data.Event[3]/yield"/>
      <org.jalgo.module.em.data.Partition reference="../../events/org.jalgo.module.em.data.Event[2]/yield"/>
    </observations>
    <p0EMState>
      <data>
        <org.jalgo.module.em.data.EMData>
          <p>
            <entry>
              <org.jalgo.module.em.data.Event reference="../../../../../../events/org.jalgo.module.em.data.Event[2]"/>
              <double>0.3555555555555555</double>
            </entry>
            <entry>
              <org.jalgo.module.em.data.Event reference="../../../../../../events/org.jalgo.module.em.data.Event"/>
              <double>0.3111111111111111</double>
            </entry>
            <entry>
              <org.jalgo.module.em.data.Event reference="../../../../../../events/org.jalgo.module.em.data.Event[3]"/>
              <double>0.17777777777777776</double>
            </entry>
            <entry>
              <org.jalgo.module.em.data.Event reference="../../../../../../events/org.jalgo.module.em.data.Event[2]/yield/elements/org.jalgo.module.em.data.Event"/>
              <double>0.15555555555555556</double>
            </entry>
          </p>
          <pForPartition>
            <entry>
              <org.jalgo.module.em.data.Partition reference="../../../../../../events/org.jalgo.module.em.data.Event/yield"/>
              <double>0.3111111111111111</double>
            </entry>
            <entry>
              <org.jalgo.module.em.data.Partition reference="../../../../../../events/org.jalgo.module.em.data.Event[3]/yield"/>
              <double>0.17777777777777776</double>
            </entry>
            <entry>
              <org.jalgo.module.em.data.Partition reference="../../../../../../events/org.jalgo.module.em.data.Event[2]/yield"/>
              <double>0.5111111111111111</double>
            </entry>
          </pForPartition>
          <pForSingleEvent>
            <entry>
              <java.awt.Point>
                <x>2</x>
                <y>1</y>
              </java.awt.Point>
              <double>0.3333333333333333</double>
            </entry>
            <entry>
              <java.awt.Point>
                <x>1</x>
                <y>1</y>
              </java.awt.Point>
              <double>0.6666666666666666</double>
            </entry>
            <entry>
              <java.awt.Point>
                <x>2</x>
                <y>0</y>
              </java.awt.Point>
              <double>0.5333333333333333</double>
            </entry>
            <entry>
              <java.awt.Point>
                <x>1</x>
                <y>0</y>
              </java.awt.Point>
              <double>0.4666666666666667</double>
            </entry>
          </pForSingleEvent>
          <likelihood>-6.151945065774981</likelihood>
        </org.jalgo.module.em.data.EMData>
        <org.jalgo.module.em.data.EMData>
          <p>
            <entry>
              <org.jalgo.module.em.data.Event reference="../../../../../../events/org.jalgo.module.em.data.Event[2]"/>
              <double>0.06666666666666667</double>
            </entry>
            <entry>
              <org.jalgo.module.em.data.Event reference="../../../../../../events/org.jalgo.module.em.data.Event"/>
              <double>0.26666666666666666</double>
            </entry>
            <entry>
              <org.jalgo.module.em.data.Event reference="../../../../../../events/org.jalgo.module.em.data.Event[3]"/>
              <double>0.13333333333333333</double>
            </entry>
            <entry>
              <org.jalgo.module.em.data.Event reference="../../../../../../events/org.jalgo.module.em.data.Event[2]/yield/elements/org.jalgo.module.em.data.Event"/>
              <double>0.5333333333333333</double>
            </entry>
          </p>
          <pForPartition>
            <entry>
              <org.jalgo.module.em.data.Partition reference="../../../../../../events/org.jalgo.module.em.data.Event/yield"/>
              <double>0.26666666666666666</double>
            </entry>
            <entry>
              <org.jalgo.module.em.data.Partition reference="../../../../../../events/org.jalgo.module.em.data.Event[3]/yield"/>
              <double>0.13333333333333333</double>
            </entry>
            <entry>
              <org.jalgo.module.em.data.Partition reference="../../../../../../events/org.jalgo.module.em.data.Event[2]/yield"/>
              <double>0.6</double>
            </entry>
          </pForPartition>
          <pForSingleEvent>
            <entry>
              <java.awt.Point>
                <x>2</x>
                <y>1</y>
              </java.awt.Point>
              <double>0.6666666666666666</double>
            </entry>
            <entry>
              <java.awt.Point>
                <x>1</x>
                <y>1</y>
              </java.awt.Point>
              <double>0.3333333333333333</double>
            </entry>
            <entry>
              <java.awt.Point>
                <x>2</x>
                <y>0</y>
              </java.awt.Point>
              <double>0.2</double>
            </entry>
            <entry>
              <java.awt.Point>
                <x>1</x>
                <y>0</y>
              </java.awt.Point>
              <double>0.8</double>
            </entry>
          </pForSingleEvent>
          <likelihood>-6.042886344241483</likelihood>
        </org.jalgo.module.em.data.EMData>
        <org.jalgo.module.em.data.EMData>
          <p>
            <entry>
              <org.jalgo.module.em.data.Event reference="../../../../../../events/org.jalgo.module.em.data.Event[2]"/>
              <double>0.1</double>
            </entry>
            <entry>
              <org.jalgo.module.em.data.Event reference="../../../../../../events/org.jalgo.module.em.data.Event"/>
              <double>0.4</double>
            </entry>
            <entry>
              <org.jalgo.module.em.data.Event reference="../../../../../../events/org.jalgo.module.em.data.Event[3]"/>
              <double>0.1</double>
            </entry>
            <entry>
              <org.jalgo.module.em.data.Event reference="../../../../../../events/org.jalgo.module.em.data.Event[2]/yield/elements/org.jalgo.module.em.data.Event"/>
              <double>0.4</double>
            </entry>
          </p>
          <pForPartition>
            <entry>
              <org.jalgo.module.em.data.Partition reference="../../../../../../events/org.jalgo.module.em.data.Event/yield"/>
              <double>0.4</double>
            </entry>
            <entry>
              <org.jalgo.module.em.data.Partition reference="../../../../../../events/org.jalgo.module.em.data.Event[3]/yield"/>
              <double>0.1</double>
            </entry>
            <entry>
              <org.jalgo.module.em.data.Partition reference="../../../../../../events/org.jalgo.module.em.data.Event[2]/yield"/>
              <double>0.5</double>
            </entry>
          </pForPartition>
          <pForSingleEvent>
            <entry>
              <java.awt.Point>
                <x>2</x>
                <y>1</y>
              </java.awt.Point>
              <double>0.5</double>
            </entry>
            <entry>
              <java.awt.Point>
                <x>1</x>
                <y>1</y>
              </java.awt.Point>
              <double>0.5</double>
            </entry>
            <entry>
              <java.awt.Point>
                <x>2</x>
                <y>0</y>
              </java.awt.Point>
              <double>0.2</double>
            </entry>
            <entry>
              <java.awt.Point>
                <x>1</x>
                <y>0</y>
              </java.awt.Point>
              <double>0.8</double>
            </entry>
          </pForSingleEvent>
          <likelihood>-6.301029995663981</likelihood>
        </org.jalgo.module.em.data.EMData>
        <org.jalgo.module.em.data.EMData>
          <p>
            <entry>
              <org.jalgo.module.em.data.Event reference="../../../../../../events/org.jalgo.module.em.data.Event[2]"/>
              <double>0.36000000000000004</double>
            </entry>
            <entry>
              <org.jalgo.module.em.data.Event reference="../../../../../../events/org.jalgo.module.em.data.Event"/>
              <double>0.04000000000000001</double>
            </entry>
            <entry>
              <org.jalgo.module.em.data.Event reference="../../../../../../events/org.jalgo.module.em.data.Event[3]"/>
              <double>0.54</double>
            </entry>
            <entry>
              <org.jalgo.module.em.data.Event reference="../../../../../../events/org.jalgo.module.em.data.Event[2]/yield/elements/org.jalgo.module.em.data.Event"/>
              <double>0.06</double>
            </entry>
          </p>
          <pForPartition>
            <entry>
              <org.jalgo.module.em.data.Partition reference="../../../../../../events/org.jalgo.module.em.data.Event/yield"/>
              <double>0.04000000000000001</double>
            </entry>
            <entry>
              <org.jalgo.module.em.data.Partition reference="../../../../../../events/org.jalgo.module.em.data.Event[3]/yield"/>
              <double>0.54</double>
            </entry>
            <entry>
              <org.jalgo.module.em.data.Partition reference="../../../../../../events/org.jalgo.module.em.data.Event[2]/yield"/>
              <double>0.42000000000000004</double>
            </entry>
          </pForPartition>
          <pForSingleEvent>
            <entry>
              <java.awt.Point>
                <x>2</x>
                <y>1</y>
              </java.awt.Point>
              <double>0.6</double>
            </entry>
            <entry>
              <java.awt.Point>
                <x>1</x>
                <y>1</y>
              </java.awt.Point>
              <double>0.4</double>
            </entry>
            <entry>
              <java.awt.Point>
                <x>2</x>
                <y>0</y>
              </java.awt.Point>
              <double>0.9</double>
            </entry>
            <entry>
              <java.awt.Point>
                <x>1</x>
                <y>0</y>
              </java.awt.Point>
              <double>0.1</double>
            </entry>
          </pForSingleEvent>
          <likelihood>-9.517728901461108</likelihood>
        </org.jalgo.module.em.data.EMData>
        <org.jalgo.module.em.data.EMData>
          <p>
            <entry>
              <org.jalgo.module.em.data.Event reference="../../../../../../events/org.jalgo.module.em.data.Event[2]"/>
              <double>1.0000000000000001E-11</double>
            </entry>
            <entry>
              <org.jalgo.module.em.data.Event reference="../../../../../../events/org.jalgo.module.em.data.Event"/>
              <double>1.0E-6</double>
            </entry>
            <entry>
              <org.jalgo.module.em.data.Event reference="../../../../../../events/org.jalgo.module.em.data.Event[3]"/>
              <double>1.0E-5</double>
            </entry>
            <entry>
              <org.jalgo.module.em.data.Event reference="../../../../../../events/org.jalgo.module.em.data.Event[2]/yield/elements/org.jalgo.module.em.data.Event"/>
              <double>1.0</double>
            </entry>
          </p>
          <pForPartition>
            <entry>
              <org.jalgo.module.em.data.Partition reference="../../../../../../events/org.jalgo.module.em.data.Event/yield"/>
              <double>1.0E-6</double>
            </entry>
            <entry>
              <org.jalgo.module.em.data.Partition reference="../../../../../../events/org.jalgo.module.em.data.Event[3]/yield"/>
              <double>1.0E-5</double>
            </entry>
            <entry>
              <org.jalgo.module.em.data.Partition reference="../../../../../../events/org.jalgo.module.em.data.Event[2]/yield"/>
              <double>1.00000000001</double>
            </entry>
          </pForPartition>
          <pForSingleEvent>
            <entry>
              <java.awt.Point>
                <x>2</x>
                <y>1</y>
              </java.awt.Point>
              <double>1.0</double>
            </entry>
            <entry>
              <java.awt.Point>
                <x>1</x>
                <y>1</y>
              </java.awt.Point>
              <double>1.0E-6</double>
            </entry>
            <entry>
              <java.awt.Point>
                <x>2</x>
                <y>0</y>
              </java.awt.Point>
              <double>1.0E-5</double>
            </entry>
            <entry>
              <java.awt.Point>
                <x>1</x>
                <y>0</y>
              </java.awt.Point>
              <double>1.0</double>
            </entry>
          </pForSingleEvent>
          <likelihood>-33.99999999996091</likelihood>
        </org.jalgo.module.em.data.EMData>
        <org.jalgo.module.em.data.EMData>
          <p>
            <entry>
              <org.jalgo.module.em.data.Event reference="../../../../../../events/org.jalgo.module.em.data.Event[2]"/>
              <double>0.24</double>
            </entry>
            <entry>
              <org.jalgo.module.em.data.Event reference="../../../../../../events/org.jalgo.module.em.data.Event"/>
              <double>0.36</double>
            </entry>
            <entry>
              <org.jalgo.module.em.data.Event reference="../../../../../../events/org.jalgo.module.em.data.Event[3]"/>
              <double>0.16000000000000003</double>
            </entry>
            <entry>
              <org.jalgo.module.em.data.Event reference="../../../../../../events/org.jalgo.module.em.data.Event[2]/yield/elements/org.jalgo.module.em.data.Event"/>
              <double>0.24</double>
            </entry>
          </p>
          <pForPartition>
            <entry>
              <org.jalgo.module.em.data.Partition reference="../../../../../../events/org.jalgo.module.em.data.Event/yield"/>
              <double>0.36</double>
            </entry>
            <entry>
              <org.jalgo.module.em.data.Partition reference="../../../../../../events/org.jalgo.module.em.data.Event[3]/yield"/>
              <double>0.16000000000000003</double>
            </entry>
            <entry>
              <org.jalgo.module.em.data.Partition reference="../../../../../../events/org.jalgo.module.em.data.Event[2]/yield"/>
              <double>0.48</double>
            </entry>
          </pForPartition>
          <pForSingleEvent>
            <entry>
              <java.awt.Point>
                <x>2</x>
                <y>1</y>
              </java.awt.Point>
              <double>0.4</double>
            </entry>
            <entry>
              <java.awt.Point>
                <x>1</x>
                <y>1</y>
              </java.awt.Point>
              <double>0.6</double>
            </entry>
            <entry>
              <java.awt.Point>
                <x>2</x>
                <y>0</y>
              </java.awt.Point>
              <double>0.4</double>
            </entry>
            <entry>
              <java.awt.Point>
                <x>1</x>
                <y>0</y>
              </java.awt.Point>
              <double>0.6</double>
            </entry>
          </pForSingleEvent>
          <likelihood>-6.235378895238716</likelihood>
        </org.jalgo.module.em.data.EMData>
      </data>
    </p0EMState>
    <experiment>
      <int>2</int>
      <int>2</int>
    </experiment>
    <frequencySum>15.0</frequencySum>
    <type>2</type>
    <par1>1</par1>
    <par2>-1</par2>
  </org.jalgo.module.em.data.StartParameters>
</object-stream>